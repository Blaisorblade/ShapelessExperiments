package shapelessexperiments

/*
//Add type members to :+: — but it breaks covariance with Scalac!

object ShapelessOpen {
  sealed trait Coproduct

  sealed trait TPlus extends Coproduct {
    type H
    type T <: Coproduct
  }

  //Not supported by Dotty.
  type :+:[H0, T0] = TPlus { type H <: H0; type T <: T0 }
  type :++:[H0, T0] = TPlus { type H = H0; type T = T0 }

  final case class Inl[+H0, +T0 <: Coproduct](head: H0) extends TPlus {
    type H <: H0
    type T <: T0
    override def toString = head.toString
  }
  //type Inl

  final case class Inr[+H0, +T0 <: Coproduct](tail: T0) extends TPlus {
    type H <: H0
    type T <: T0
    override def toString = tail.toString
  }


  sealed trait CNil extends Coproduct
}

//import shapeless.Generic
trait Generic[T] {
  type Repr
  def to(t : T) : Repr
  def from(r : Repr) : T
}

import ShapelessOpen._
*/
import shapeless._

//sealed trait Term
//case class A() extends Term
//case class B() extends Term
//case class C(x: Int) extends Term
//
//object Base {
//  //implicit val v: Generic[Term] = implicitly
//  //implicit val v = Generic[Term]
//}

//Doesn't work, the Generic macro breaks this badly:
//object Base {
//  sealed trait Term
//  case class A() extends Term
//  case class B() extends Term
//  //implicit val v: Generic[Term] = implicitly
//  implicit val v = Generic[Term]
//}

object TestGeneric {

  trait Base {
    outer =>
    trait Term
    case class A() extends Term
    case class B() extends Term
    //Recursive implicits are bad!
    //implicit val v: Generic[Term] = implicitly
    //implicit val v = Generic[Term]

    //    type ToGenericRest <: Coproduct
    //    type ToGeneric = A :+: B :+: ToGenericRest

    //What I want for extensibility, but does not make sense:
    //    type ToGeneric <: A :+: B :+: Coproduct
    type ToGeneric >: A :+: B :+: Nothing <: A :+: B :+: Coproduct

    //Not working by Scalac, but working for Dotty.
//    type ToGeneric >: TPlus {
//      type H <: A
//      type T <: TPlus {
//        type H <: B
//        type T <: Coproduct
//      }
//    } <: TPlus {
//      type H <: A
//      type T <: TPlus {
//        type H <: B
//      }
//    }

    /*
    //Not supported by Dotty.
    type ToGenericBase[Rest <: Coproduct] = TPlus {
      type H <: A
      type T <: TPlus {
        type H <: B
        type T <: Rest
      }
    }
    */
//    //implicitly[TPlus { type H]
//    //implicitly[TPlus { type H <: A; type T <: Nothing } <:< TPlus { type H <: A; type T <: TPlus { type H <: B } }]
//    //implicitly[TPlus { type H <: A; type T <: TPlus } <:< TPlus { type H <: A; type T <: TPlus { type H <: B } }]
//    implicitly[TPlus { type H <: A; type T <: TPlus { type H <: Nothing } } <:< TPlus { type H <: A; type T <: TPlus { type H <: B } }]
//    implicitly[Nothing <:< ToGeneric]

    //What the fuck? See bug report.
    //implicitly[TPlus { type T <: Nothing } <:< TPlus { type T <: TPlus {type H <: A} } ]
    def to(t: Term): ToGeneric =
      t match {
        case t1 @ A() =>
          //((Inl[ToGeneric#H, Nothing](t1): Inl[ToGeneric#H, ToGeneric#T]): ToGeneric#H :+: ToGeneric#T): ToGeneric#H :++: ToGeneric#T
          //(Inl[ToGeneric#H, Nothing](t1): ToGeneric#H :+: ToGeneric#T): ToGeneric#H :++: ToGeneric#T
          //DOTTY can figure this out, Scalac can't.
          //Inl(t1)//: TPlus { type H <: A; type T <: TPlus { type H <: B } }

          //Does not work
          //Coproduct[ToGeneric](t1)
          //Works
          Inl(t1)
        case t2 @ B() =>
          Inr(Inl(t2))
          //???
      }
    //def toRest(t: Coproduct): ToGeneric = Inr(Inr(t))
    def from(t: ToGeneric): Term = ???

    implicit val genericTerm: Generic[Term] = new Generic[Term] {
      type Repr = ToGeneric
      def to(t: Term): Repr = outer.to(t)
      def from(t: Repr): Term = outer.from(t)
    }
  }

  trait Derived1 extends Base {
    case class C() extends Term
    type ToGeneric >: A :+: B :+: C :+: Nothing <: A :+: B :+: C :+: Coproduct
//    type ToGenericRest = C :+: Coproduct
//    type ToGeneric = A :+: B :+: C :+: Coproduct
    //Argh! What about independent extensions?
    //We need overriding for functions...
    /*type ToGeneric >: TPlus {
      type H <: A
      type T <: TPlus {
        type H <: B
        type T <: TPlus {
          type H <: C
          type T >: Nothing <: Coproduct
        }
      }
    } <: TPlus {
      type H <: A
      type T <: TPlus {
        type H <: B
        type T <: TPlus {
          type H <: C
        }
      }
    }
    override def to(t: Term): ToGeneric =
      t match {
        case t1 @ C() =>
          super.toRest(Inl(t1))
      }*/
    override def from(t: ToGeneric): Term = ???
  }

  trait Derived2 extends Base {
    case class D() extends Term
  }
}
