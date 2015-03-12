package org.hymao.query

import java.io.File
import org.hymao.query.Vocab._
import org.phenoscape.scowl.OWL._
import org.phenoscape.owlet.SPARQLComposer._
import org.phenoscape.owlet.OwletManchesterSyntaxDataType.SerializableClassExpression
import com.hp.hpl.jena.vocabulary.RDF
import scala.collection.JavaConversions._
import org.apache.log4j.BasicConfigurator
import org.apache.log4j.Logger
import org.apache.log4j.Level

object SemExample extends App {
  BasicConfigurator.configure()
  Logger.getRootLogger.setLevel(Level.ERROR)

  val store = new SemStore(new File("onts"))

  val states = store.reasoner.getInstances(denotes only (has_part some (bearer_of some Color)),
    false).getFlattened
  println(states)
  println(store.reasoner.getSubClasses((has_part some (bearer_of some Color)), false).getFlattened)

  val statesQuery = select('state, 'state_label) where (
    bgp(
      t('state, rdfsLabel, 'state_label),
      t('state, rdfType, 'restriction),
      t('restriction, owlOnProperty, denotes),
      t('restriction, owlAllValuesFrom, 'phenotype),
      t('phenotype, rdfsSubClassOf, (has_part some (bearer_of some Color)).asOMN)))

  store.select(statesQuery).foreach { solution =>
    println(solution)
  }
}