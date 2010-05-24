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
  //override def skipEmailValidation = true
  
  override def localForm(user: User, ignorePassword: Boolean): NodeSeq = {
    /* This doesn't work either
    for {
      f <- signupFields
    } yield
      <tr><td>{f.displayName}</td><td>{f.toForm}</td></tr>
    */
    val formXhtml: NodeSeq = {
      <tr><td>{user.firstName.toForm}</td></tr>
      <tr><td>{user.lastName.toForm}</td></tr>
      <tr><td>{user.email.toForm}</td></tr>
      <tr><td>{user.locale.toForm}</td></tr>
      <tr><td>{user.timezone.toForm}</td></tr>
    }

    if (!ignorePassword)
      formXhtml ++ <tr><td>{user.password.toForm}</td></tr>
    else
      formXhtml
  }
}

/**
 * A "User" class that includes first name, last name, password
 */
class User extends MegaProtoUser[User] {  
  def meta = User // what's the "meta" server
}

}
}
