const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

exports.searchUserByEmail = functions.https.onCall(async (req) => {
  const email = req.query.email;

  try {
    const userRecord = await admin.auth().getUserByEmail(email);
    return {exists: true, uid: userRecord.uid};
  } catch (error) {
    return {exists: false};
  }
});
