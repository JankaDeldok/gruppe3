const mongoose = require('mongoose');
const Challenge = require('./challengeSchema');
const Schema = mongoose.Schema;

/**
 * Represents a user for the tudas app.
 */
const userSchema = new Schema({
    // name of the user
    name: {
        type: String,
        required: true,
        unique: true
    },
    // encrypted password of the user
    password: {
        type: String,
        required: true,
    },
    // emailaddress of the suer
    emailAddress: {
        type: String,
        required: true,
        unique: true
    },
    // the URI of a profile picture of the user
    profilePicture: {
        type: String
    },
    // total challenge points of the user 
    points: Number,
    // total credit points of the user 
    credit: Number,
    // friends of the user, one-to-few relationship. name = friend name, streak = number of days challenging each other in a row
    friends: [{ name: String, streak: Number }],
    // challenges the user created, many-to-many relationship. References a challenge
    createdChallenges: [{ type: Schema.Types.ObjectId, ref: 'Challenge' }],
    // challenges the user has to fulfill, many-to-many relationship. References a challenge
    openChallenges: [{ type: Schema.Types.ObjectId, ref: 'Challenge' }],
    // challenges the user finished, many-to-many relationship. References a challenge
    finishedChallenges: [{ type: Schema.Types.ObjectId, ref: 'Challenge' }],
    // the feed of the user
    feed: [{ message: String, new: Boolean }]
});


const User = mongoose.model('User', userSchema);
module.exports = User;