package code {
package model {

import scala.xml.{NodeSeq, Text}

import net.liftweb.common._
import net.liftweb.util._
import net.liftweb.http._
import net.liftweb.util.Helpers._
import net.liftweb.record.field.PasswordField

import lib._

/**
* The singleton that has methods for accessing the database
*/
object User extends User with MetaMegaProtoUser[User] {
  override def collectionName = "users" // define the MongoDB collection name
  override def screenWrap = Full(<lift:surround with="default" at="content">
            <lift:bind /></lift:surround>)
  override def skipEmailValidation = true // uncomment this line to skip email validations

  override def localForm(user: User, ignorePassword: Boolean): NodeSeq = {
    /* This doesn't work either
    for {
      f <- signupFields
    } yield
      <tr><td>{f.displayName}</td><td>{f.toForm}</td></tr>
    */
    val formXhtml: NodeSeq = {
      <tr><td>{user.firstName.displayName}</td><td>{user.firstName.toForm openOr Text("")}</td></tr>
      <tr><td>{user.lastName.displayName}</td><td>{user.lastName.toForm openOr Text("")}</td></tr>
      <tr><td>{user.email.displayName}</td><td>{user.email.toForm openOr Text("")}</td></tr>
      <tr><td>{user.locale.displayName}</td><td>{user.locale.toForm openOr Text("")}</td></tr>
      <tr><td>{user.timezone.displayName}</td><td>{user.timezone.toForm openOr Text("")}</td></tr>
    }

    if (!ignorePassword)
      formXhtml ++ <tr><td>{user.password.displayName}</td><td>{user.password.toForm openOr Text("")}</td></tr>
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
