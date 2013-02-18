package edu.washington.cs.knowitall.relgrams

/**
 * Created with IntelliJ IDEA.
 * User: niranjan
 * Date: 2/17/13
 * Time: 10:23 PM
 * To change this template use File | Settings | File Templates.
 */


import scala.collection.JavaConversions._
import collection.parallel.mutable

case class RelationTuple(arg1:String, rel:String, arg2:String, var hashes:Set[Int], var arg1HeadCounts:scala.collection.mutable.Map[String,Int], var arg2HeadCounts:scala.collection.mutable.Map[String,Int])
case class Relgram(first:RelationTuple, second:RelationTuple)
case class RelgramCounts(relgram:Relgram, counts:scala.collection.mutable.Map[Int, Int])


object RelgramCountingUtils {

def updateHashes(rgc: RelgramCounts,
                 headFirst: String, headSecond: String,
                 firstHashes: Set[Int], secondHashes:Set[Int]) {
  val (fh:Set[Int], sh:Set[Int]) = lexicalOrder(headFirst, headSecond, firstHashes, secondHashes)
  rgc.relgram.first.hashes ++= fh
  rgc.relgram.second.hashes ++= sh
}



/**
 * Return a tuple in which the firstValue is the first element if the firstKey is lexically smaller than the second key.
 */
def lexicalOrder[T](firstKey: String, secondKey: String, firstValue:T, secondValue:T): (T, T) = {
if (firstKey.compareTo(secondKey) > 0) (secondValue, firstValue) else (firstValue, secondValue)
}

def lexicallySortedTuple(first: String, second: String): (String, String) = lexicalOrder(first, second, first, second).asInstanceOf[(String, String)]



}

