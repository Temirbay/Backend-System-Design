
object Test extends App{

  def getInfo (name : String) : String = name match {

    case "b" | "a" => "a | b"
    case "a" => "a"
  }

}

