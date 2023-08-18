package com.petpal.swimmer_seller.data.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.petpal.swimmer_seller.data.model.Product

class ProductRepository {
    companion object {
        // 상품 인덱스 값 가져오기
        fun getProductIdx(callback: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val productIdxRef = database.getReference("productIdx")
            productIdxRef.get().addOnCompleteListener(callback)
        }

        // 상품 인덱스(일괄 관리용 순서 번호 인덱스) 값 저장
        fun setProductIdx(productIdx: Long, callback: (Task<Void>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val productIdxRef = database.getReference("productIdx")
            productIdxRef.get().addOnCompleteListener {
                it.result.ref.setValue(productIdx).addOnCompleteListener(callback)
            }
        }

        // 상품 정보 저장
        fun addProduct(product: Product, callback: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val productDataRef = database.getReference("product")
            productDataRef.push().setValue(product).addOnCompleteListener(callback)
        }

        // 상품 정보 수정
        fun modifyProduct(product: Product, isNewImage: Boolean, callback: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val productDataRef = database.getReference("product")

            productDataRef.orderByChild("productIdx").equalTo(product.productIdx.toDouble()).get().addOnCompleteListener {
                for (dataSnapshot in it.result.children) {
                    // 수정 가능한 항목만 값 수정
                    // code, sellerIdx, orderCount, regDate 등은 고정 값 / reviewList는 판매자가 조작할 수 없는 값
                    if (isNewImage) {
                        dataSnapshot.ref.child("mainImage").setValue(product.mainImage)
                        dataSnapshot.ref.child("descriptionImage").setValue(product.descriptionImage)
                    }
                    dataSnapshot.ref.child("name").setValue(product.name)
                    dataSnapshot.ref.child("price").setValue(product.price)
                    dataSnapshot.ref.child("description").setValue(product.description)
                    dataSnapshot.ref.child("sizeList").setValue(product.sizeList)
                    dataSnapshot.ref.child("colorList").setValue(product.colorList)
                    dataSnapshot.ref.child("hashTag").setValue(product.hashTag)
                    dataSnapshot.ref.child("category").setValue(product.category).addOnCompleteListener(callback)
                }
            }
        }

        // 상품 정보 삭제
        fun deleteProduct(productIdx: Long, callback: (Task<Void>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val productDataRef = database.getReference("product")

            productDataRef.orderByChild("productIdx").equalTo(productIdx.toDouble()).get().addOnCompleteListener {
                for (dataSnapshot in it.result.children) {
                    dataSnapshot.ref.removeValue().addOnCompleteListener(callback)
                }
            }
        }

        // 특정 판매자가 등록한 상품 목록 가져오기
        fun getProductListBySellerIdx(sellerIdx: Long, callback: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val productDataRef = database.getReference("product")

            productDataRef.orderByChild("sellerIdx").equalTo(sellerIdx.toDouble())
                .ref.orderByChild("productIdx").get().addOnCompleteListener(callback)
        }

        // 특정 파일명으로 이미지 업로드
        fun uploadImage(uploadUri: Uri, fileName: String, callback: (Task<UploadTask.TaskSnapshot>) -> Unit) {
            val storage = FirebaseStorage.getInstance()
            val imageRef = storage.reference.child(fileName)
            imageRef.putFile(uploadUri).addOnCompleteListener(callback)
        }

        // 특정 파일명의 이미지 다운로드
        fun downloadImage(fileName: String, callback: (Task<Uri>) -> Unit){
            val storage = FirebaseStorage.getInstance()
            val imageRef = storage.reference.child(fileName)
            imageRef.downloadUrl.addOnCompleteListener(callback)
        }
    }
}