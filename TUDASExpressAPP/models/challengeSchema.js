const mongoose = require('mongoose');
const Schema = mongoose.Schema;

/**
 * Represents a challenge for the tudas app.
 */
const challengeSchema = new Schema({
    // name of the challenge
    name: {
        type: String,
        required: true,
        unique: true
    },
    // description of the challenge
    description: {
        type: String,
        required: true,
    },
    // the user who created the challenge
    creator: {
        type: Schema.Types.ObjectId,
        ref: 'User',
        required: true,
    },
    // the creation date of the challenge
    creationDate: {
        type: Date,
        required: true,
    },
    // the due date of the challenge
    dueDate: {
        type: Date,
    },
    // the URI of a picture or video as proof for succeeding in a challenge
    proofMedia: {
        type: String
    },
    // the points a number is worth, depending on the difficulty
    worth: Number,
});


const Challenge = mongoose.model('Challenge', challengeSchema);
module.exports = Challenge;