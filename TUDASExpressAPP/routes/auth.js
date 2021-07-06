
var express = require('express');
var auth = express.Router();

const bcrypt = require('bcryptjs');
const config = require('config');
const jwt = require('jsonwebtoken')

const User = require('../models/userSchema');


/* POST /auth/login */
/* Authenticate the User by name and password*/
auth.post('/login', async (req, res) => {
    const { name, password } = req.body;

    try {
        // Check if user name exists
        const user = await User.findOne({ name });
        if (!user) throw Error('User does not exist. Please Sign Up!');

        //compare the given password with the password in the db 
        const isMatch = await bcrypt.compare(password, user.password);
        if (!isMatch) throw Error('Invalid password');

        //create jwt token and sign the user id to it
        const token = jwt.sign({ id: user._id }, config.get('jwtSecret'));
        if (!token) throw Error('Couldnt sign the token');

        //return the token and user as JSON
        res.status(200).json({
            token,
            user: {
                id: user._id,
                name: user.name,
                email: user.emailAddress
            }
        });
    } catch (e) {
        res.status(400).json({ msg: e.message });
    }
});

/* POST /auth/register */
/* Registers a new User */
auth.post('/register', async (req, res) => {
    const { name, password, email } = req.body;
    try {
        //check if the username is alreay taken
        const user = await User.findOne({ name });
        if (user) throw Error('User already exists');

        //encrypt the password
        const salt = await bcrypt.genSalt(10);
        if (!salt) throw Error('Something went wrong with bcrypt');
        const hash = await bcrypt.hash(password, salt);
        if (!hash) throw Error('Something went wrong hashing the password');

        const newUser = new User({
            name: name,
            password: hash,
            emailAddress: email,
            credit: 1000,
            points: 0
        });

        //save the user to the db
        const savedUser = await newUser.save();
        if (!savedUser) throw Error('Something went wrong saving the user');

        //create jwt token that lasts 24 hour and sign the user id to it
        const token = jwt.sign({ id: savedUser._id }, config.get('jwtSecret'));

        //return the token and the user as JSON
        console.log(savedUser.emailAddress)
        console.log("---")
        res.status(200).json({
            token,
            user: {
                id: savedUser.id,
                name: savedUser.name,
                email: savedUser.emailAddress
            }
        });
    } catch (e) {
        res.status(400).json({ msg: e.message });
    }
});

module.exports = auth;