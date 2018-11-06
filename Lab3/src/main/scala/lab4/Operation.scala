package lab4


/**
  * Enum for operands. Only 4 operands are supported for now:
  * - addition
  * - subtraction
  * - multiplication
  * - division
  */

sealed trait Operation

object Operation {
  case object ADD extends Operation
  case object SUBTRACT extends Operation
  case object MULTIPLY extends Operation
  case object DIVIDE extends Operation
}