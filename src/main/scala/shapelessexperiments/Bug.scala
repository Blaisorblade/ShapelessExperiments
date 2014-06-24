//Disabled to avoid declarations conflicting with the main source.
//This is SI-8678.
//
//package shapelessexperiments
//
//sealed trait Coproduct
//
//sealed trait TPlus extends Coproduct {
//  type H
//  type T <: Coproduct
//}
//
//object Bug {
//  sealed trait Term
//  case class A() extends Term
//  case class B() extends Term
//  case class C(x: Int) extends Term
//
//  implicitly[TPlus { type T <: Nothing } <:< TPlus { type T <: TPlus } ]
//  //implicitly[TPlus { type T <: Nothing } <:< TPlus { type T <: TPlus {type H <: A} } ] //FAILS!
//  implicitly[Nothing <:< TPlus {type H <: A} ] //But it should only recurse on this, which succeeds!
//  implicitly[TPlus { type T <: TPlus {type H <: Nothing} } <:< TPlus { type T <: TPlus {type H <: A} } ]
//
//  implicitly[TPlus { type H <: A; type T <: TPlus { type H <: Nothing } } <:< TPlus { type H <: A; type T <: TPlus { type H <: B } }]
//}
