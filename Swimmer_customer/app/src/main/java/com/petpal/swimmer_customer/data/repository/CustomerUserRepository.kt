package com.petpal.swimmer_customer.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.petpal.swimmer_customer.data.model.User

class CustomerUserRepository : UserRepository {
    private val mAuth: FirebaseAuth
    private val mDatabase: DatabaseReference

    init {
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference("users")
    }
    override fun addUser(user: User?): LiveData<Boolean?>? {
        val resultLiveData = MutableLiveData<Boolean?>()
        mAuth.createUserWithEmailAndPassword(user?.email!!, user.password!!)

            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    // 회원가입이 성공했을 때 Firebase Realtime Database에 유저 정보 저장
                    val firebaseUser = mAuth.currentUser
                    if (firebaseUser != null) {
                        //비밀번호는 db에 올리지 않는다.
                        user.password = null
                        mDatabase.child((user.uid).toString())
                            .setValue(user)
                            .addOnCompleteListener { dbTask: Task<Void?> ->
                                if (dbTask.isSuccessful) {
                                    resultLiveData.setValue(true)
                                } else {
                                    // DB 저장 실패시
                                    resultLiveData.setValue(false)
                                }
                            }
                    } else {
                        resultLiveData.setValue(false)
                    }
                } else {
                    // 회원가입 실패시
                    resultLiveData.setValue(false)
                }
            }
        return resultLiveData
    }

    override fun signInUser(email: String, password: String): LiveData<Boolean?> {
        val resultLiveData = MutableLiveData<Boolean>()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    resultLiveData.setValue(true)
                } else {
                    resultLiveData.setValue(false)
                }
            }
        return resultLiveData
    }

//    override fun getUserByIdx(userIdx: String?): LiveData<User?> {
//        val result = MutableLiveData<User?>()
//        mDatabase.child(userIdx!!).addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    val user = dataSnapshot.getValue(
//                        User::class.java
//                    )
//                    result.setValue(user)
//                } else {
//                    result.setValue(null)
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                result.value = null
//            }
//        })
//        return result
//    }

    override fun checkEmailDuplicated(email: String?): LiveData<Boolean> {

        val emailExists = MutableLiveData<Boolean>()
        Log.d("targetEmail",email!!)
        mDatabase.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        emailExists.setValue(true)
                    } else {
                        emailExists.setValue(false)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    emailExists.value = false
                }
            })
        return emailExists
    }
    //    @Override
    //    public LiveData<Boolean> modifyUser(String userIdx, String email, String password) {
    //        MutableLiveData<Boolean> result = new MutableLiveData<>();
    //
    //        //serIdx에 해당하는 사용자의 정보를 가져옴
    //        mDatabase.child(userIdx).addListenerForSingleValueEvent(new ValueEventListener() {
    //            @Override
    //            public void onDataChange(DataSnapshot dataSnapshot) {
    //                User user = dataSnapshot.getValue(User.class);
    //                if (user != null) {
    //                    // 가져온 사용자 정보를 수정
    //                    user.setEmail(email);
    //                    user.setPassword(password);
    //
    //                    // 수정된 사용자 정보를 데이터베이스에 저장
    //                    mDatabase.child(userIdx).setValue(user).addOnCompleteListener(task -> {
    //                        result.setValue(task.isSuccessful());
    //                    });
    //                } else {
    //                    result.setValue(false); // userIdx에 일치하는 사용자 정보가 없음
    //                }
    //            }
    //
    //            @Override
    //            public void onCancelled(DatabaseError databaseError) {
    //                result.setValue(false);
    //            }
    //        });
    //
    //        return result;
    //    }
    override fun deleteUser(userIdx: String?): LiveData<Boolean?>? {
        val result = MutableLiveData<Boolean?>()
        mDatabase.child(userIdx!!).removeValue()
            .addOnCompleteListener { task: Task<Void?> -> result.setValue(task.isSuccessful) }
        return result
    }

    override fun findEmailbyInfo(nickname: String?, phoneNumber: String?): LiveData<User?>? {
        val result = MutableLiveData<User?>()

        // 폰번호가 일치하는 사용자를 찾는 쿼리를 작성합니다.
        mDatabase.orderByChild("phoneNumber").equalTo(phoneNumber)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (childSnapshot in dataSnapshot.children) {
                        val user = childSnapshot.getValue(
                            User::class.java
                        )
                        if (user != null) {
                            result.value = user // 일치하는 사용자 발견 시 해당 사용자 정보를 설정합니다.
                            return
                        }
                    }
                    result.value = null // 일치하는 사용자를 찾지 못했을 때 null을 설정합니다.
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    result.value = null
                }
            })
        return result
    }


    override fun resetPassword(email: String?, phoneNumber: String?): LiveData<Boolean?>? {
        val resultLiveData = MutableLiveData<Boolean?>()

        // DB에서 이메일로 users 하위의 모든 노드에서 email 속성을 검사
        mDatabase.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val user = userSnapshot.getValue(
                                User::class.java
                            )
                            if (user != null && user.phoneNumber == phoneNumber) {
                                // 이메일과 핸드폰 번호가 일치하는 사용자를 찾음
                                FirebaseAuth.getInstance().sendPasswordResetEmail(email!!)
                                    .addOnCompleteListener { task: Task<Void?> ->
                                        if (task.isSuccessful) {
                                            resultLiveData.setValue(true)
                                        } else {
                                            resultLiveData.setValue(false)
                                        }
                                    }
                                return
                            }
                        }
                    }
                    resultLiveData.value = false // 일치하는 사용자를 찾지 못함
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    resultLiveData.value = false
                }
            })
        return resultLiveData
    }
}