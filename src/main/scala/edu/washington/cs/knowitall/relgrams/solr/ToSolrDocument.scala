package edu.washington.cs.knowitall.relgrams.solr

/**
 * Created with IntelliJ IDEA.
 * User: niranjan
 * Date: 3/11/13
 * Time: 4:07 PM
 * To change this template use File | Settings | File Templates.
 */
import org.slf4j.LoggerFactory

import io.Source

import edu.washington.cs.knowitall.relgrams.RelgramCounts
import xml.Elem
import dispatch.Http

object ToSolrDocument {
   val logger = LoggerFactory.getLogger(this.getClass)

  /**
   *
  <add>
  <doc>
    <field name="id">SOLR1000</field>
    <field name="farg1">First arg1</field>
    <field name="frel">eats</field>
    <field name="farg2">second</field>
    <field name="sarg1">Second arg1</field>
    <field name="srel">also eats</field>
    <field name="sarg2">second arg2</field>
    <field name="count">10</field>
  </doc>
  </add>
   */
  var id = 0
  def toSolrXML(rgc: RelgramCounts) = {
    val farg1 = rgc.relgram.first.arg1
    val frel = rgc.relgram.first.rel
    val farg2 = rgc.relgram.first.arg2
    val sarg1 = rgc.relgram.second.arg1
    val srel = rgc.relgram.second.rel
    val sarg2 = rgc.relgram.second.arg2

    val countsString = rgc.counts.keys.toSeq.sortBy(w => w).map(w => rgc.counts.getOrElse(w, 0))
    <add>
      <doc>
        <field name="id">{id}</field>
        <field name="farg1">{farg1}</field>
        <field name="frel">{frel}</field>
        <field name="farg2">{farg2}</field>
        <field name="sarg1">{sarg1}</field>
        <field name="srel">{srel}</field>
        <field name="sarg2">{sarg2}</field>
        <field name="counts">{countsString}</field>
      </doc>
    </add>
  }

  val http = new Http
  import dispatch._
  def addToIndex(doc:Elem, solrBasePath:String) = {
    val svc = url(solrBasePath) / "solr" / "update" << Map("xml", doc.toString())
    http(svc OK as.Response(r => Source.fromInputStream(r.getResponseBodyAsStream()).getLines))
  }


  def main(args:Array[String]){

    val inputPath = args(0)
    val solrPath = args(1)
    Source.fromFile(inputPath).getLines().foreach(line => {
      RelgramCounts.fromSerializedString(line) match {
        case Some(rgc:RelgramCounts) => {
          val docXML: Elem = toSolrXML(rgc)
          val response = addToIndex(docXML, solrPath)
          println("response: " + response)
        }
        case None =>
      }
    })

  }
}
