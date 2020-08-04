const chalk = require('chalk')
const notes = require('./notes.js')

notes()
console.log(chalk.green.bold.bgBlackBright('Success!!'))