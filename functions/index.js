const {onCall, HttpsError} = require("firebase-functions/v2/https");
const admin = require("firebase-admin");
admin.initializeApp();

exports.searchUserByEmail = onCall(async (req) => {
  const email = req.query.email;

  try {
    const userRecord = await admin.auth().getUserByEmail(email);
    return {user: userRecord};
  } catch (error) {
    throw new HttpsError("user-not-found", error.message, error);
  }
});
