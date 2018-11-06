package lab4


/**
  * Represents a student
  *
  * @param firstName first name of a student
  * @param middleName optinal middle name of a student
  * @param lastName last name of a student
  * @param drivingLicenseNo optional driving license number
  */
case class Student(firstName: String,
                   middleName: Option[String],
                   lastName: String,
                   drivingLicenseNo: Option[String]) {

  /**
    * if student has a middle name
    * @return Returns `true` if student has middleName and
    *         `false` if student does not have a middleName
    */
  def hasMiddleName: Boolean = middleName match {
    case Some(_) => true
    case None => false
  }

  /**
    * if student has a driving license
    * @return
    */
  def hasDrivingLicense: Boolean = drivingLicenseNo match {
    case Some(_) => true
    case None => false
  }
}


object Lab4 extends App {

  /** ************************ Options *************************/

  // 1) What is Option? Why is it needed?

  // 2) Implement `hasMiddleName` method in `Student` case class

  // 3) Implement `hasDrivingLicense` method in `Student` case class

  // 4) Implement `addOptions` method. Return some value ONLY if both options have values.

  /**
    * Adds two options.
    *
    * @param option1
    * @param option2
    * @return
    */
  def addOptions(option1: Option[Int],
                 option2: Option[Int]): Option[Int] = {
    try {
      (option1, option2) match {
        case (Some(value1), Some(value2)) => Some(value1 + value2)
      }
    }
    catch {
      case _: Exception => None
    }
  }

  // 4) Look at `extractDouble` method and understand how it works

  /**
    * Extracts a Double from a String
    *
    * @param number string representation of Double number
    * @return Returns `Some(double)` if extraction is successful. Otherwise, when any error occurs, returns `None`.
    */
  def extractDouble(number: String): Option[Double] =
    try {
      Some(number.toDouble)
    } catch {
      // `_` means any. This line means:
      // "If any error is encountered, return `None`"
      case _: Throwable => None
    }

  // 5) Implement `extractOperand` method

  /**
    * Extracts an Operand from a String
    *
    * @param operand string representation of Operand
    * @return Returns `Some(operand)` if extraction is successful. Otherwise, when any error occurs, returns `None`.
    */
  def extractOperation(operand: String): Option[Operation] =
    try {
      operand match {
        case "ADD" => Some(Operation.ADD)
        case "SUBTRACT" => Some(Operation.SUBTRACT)
        case "MULTIPLY" => Some(Operation.MULTIPLY)
        case "DIVIDE" => Some(Operation.DIVIDE)
      }
    } catch {
      case _: Throwable => None
    }

  /**
    * 6) Now you can implement `simpleCalculate` method.
    * Remember to use `extractDouble` and `extractOperand` methods to help you validate input data
    *
    */

  /**
    * This is a simple calculator. It can add, subtract, multiply and divide two numbers.
    *
    * @param operand1  first operand
    * @param operation should one of `Operand`
    * @param operand2  second operand
    * @return Optional Double. If calculation is successful, returns `Some(double)`. Otherwise, when any
    *         error or exception occurs, returns `None`.
    */
  def simpleCalculate(operand1: String,
                      operation: String,
                      operand2: String): Option[Double] = {
    val op1: Option[Double] = extractDouble(operand1)
    val op2: Option[Double] = extractDouble(operand2)

    val operationOption: Option[Operation] = extractOperation(operation)

    val result: Option[Double] = operationOption.flatMap { operationValue =>
      operationValue match {
        case Operation.ADD => (op1, op2) match {
          case (Some(v1), Some(v2)) => Some(v1 + v2)
          case (_, _) => None
        }

        case Operation.SUBTRACT => (op1, op2) match {
          case (Some(v1), Some(v2)) => Some(v1 - v2)
          case (_, _) => None
        }

        case Operation.MULTIPLY => (op1, op2) match {
          case (Some(v1), Some(v2)) => Some(v1 * v2)
          case (_, _) => None
        }

        // Do not forget to check the case when operand2 is ZERO
        case Operation.DIVIDE => (op1, op2) match {
          case (Some(_), Some(v2)) if v2 == 0 => None
          case (Some(v1), Some(v2)) => Some(v1 / v2)
          case (_, _) => None
        }
      }
    }

    // return `result`
    result
  }

}