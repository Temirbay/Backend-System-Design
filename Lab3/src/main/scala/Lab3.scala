case class FullName(firstName: String, secondName: String)

sealed trait Human
case class Student(name: String) extends Human

case class Teacher(id: String) extends Human

case class Character(fullName: FullName, age: Int) extends Human

object Lab3 extends App {

  /**
    * Part 1
    */

  val aigul = Student("Aigul")
  val ainur = Student("Ainur")

  val students: List[Student] =
    List(aigul, ainur, Student("Azamat"), Student("Arman"), Student("Aru"))

  //  1) map students to a collection of teachers.
  // Every student name is teacher's id
  val teachers: List[Teacher] =
    students.map {student => Teacher (student.name)}

  //  2) using foreach print all students
  students.foreach(student => println(student))

  //  3) filter out students with name Azamat and Arman,
  // and using foreach print out only names of students
  students
    .filter(student => student.name == "Azamat" || student.name == "Arman")
    .foreach(student => println(student))

  /**
    * Part 2
    *
    * Disclaimer: all characters are fictional, any resemblance
    * with Game of Thrones characters is accidental and unintentional.
    *
    * Print out = in all exercises below, print using `foreach` and passing `println` method
    *
    */
  val daenerys = Character(FullName("Daenerys", "Targaryen"), 28)
  // he knows nothing :(
  val jon     = Character(FullName("Jon", "Snow"), 30)
  val tyrion  = Character(FullName("Tyrion", "Lannister"), 40)
  val petyr   = Character(FullName("Petyr", "Baelish"), 38)
  val drogo   = Character(FullName("Khal", "Drogo"), 24)
  val eddard  = Character(FullName("Eddard", "Stark"), 49)
  val arya    = Character(FullName("Arya", "Stark"), 15)
  val cercei  = Character(FullName("Cersei", "Lannister"), 42)
  val joffrey = Character(FullName("Sansa", "Stark"), 20)
  val sandor  = Character(FullName("Sandor", "Clegane"), 40)

  val characters = List(daenerys, jon, tyrion, petyr, drogo, eddard, cercei, joffrey, sandor)
  // 4) print out characters whose `secondName` is "Lannister"
  characters
    .filter(item => item.fullName.secondName.equals("Lannister"))
    .foreach(item => println(item))

  // 5) print out only `firstName`s of characters whose `secondName` is Stark
  characters
    .filter(item => item.fullName.secondName.equals("Stark"))
    .foreach(item => println(item.fullName.firstName))

  // 6) map allCharacters to `Student`s using map, where each character
  // `firstName` is student's name
  val characterStudents: List[Student] =
    characters.map(character => Student(character.fullName.firstName))

  // 7) filter out characters who are older than 20 ages old, get only their
  // `firstNames` result should be List("Daenerys", "Jon", "Tyrion", "Petyr", etc.
  val firstNamesOfCharactersOlderThanTwenty: List[String] =
    characters
      .filter(character => character.age > 20)
      .map(character => character.fullName.firstName)

  // 8) complete the getInfo method, it should return a string
  // for Student   -- student's name
  // for Teacher   -- teacher's id
  // for Character -- character's in format:
  // firstName secondName, age: character's age
  //                  ex: Eddard Stark, aged: 49
  //                  ex: Petyr  Baelish, aged: 38
  def getInfo(human: Human): String = human match {

    case Student(name) =>
      name

    case Teacher(id) =>
      s"$id"

    case Character(fullName, age) =>
      s"${fullName.firstName} ${fullName.secondName}, aged: ${age}"
  }

  characters.foreach(item => println(getInfo(item)))
  // 9) Flatten the following List of Lists. Notice that it
  // should be List of Humans

  val allLists: List[List[Human]] = List(students, teachers, characters)

  val all: List[Human] =
    allLists.flatten
}
