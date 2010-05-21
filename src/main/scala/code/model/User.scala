package code {
package model {

import scala.xml.{NodeSeq, Text}

import _root_.net.liftweb.common._
import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.record.field.PasswordField

import lib._

/**
 * The singleton that has methods for accessing the database
 */
object User extends User with MetaMegaProtoUser[User] {
  def createRecord = new User
  override def collectionName = "users" // define the MongoDB collection name
  override def screenWrap = Full(<lift:surround with="default" at="content">
			       <lift:bind /></lift:surround>)
  // comment this line out to require email validations
  override def skipEmailValidation = true  
}

/**
 * A "User" class that includes first name, last name, password
 */
class User extends MegaProtoUser[User] {  
  def meta = User // what's the "meta" server
}

}
}
