package org.hymao.query

import org.phenoscape.owl.util.OntologyUtil
import org.semanticweb.owlapi.model.OWLClassExpression
import org.phenoscape.scowl.OWL._
import org.semanticweb.owlapi.model.OWLClass
import org.hymao.query.Vocab._
import com.hp.hpl.jena.rdf.model.Model
import org.phenoscape.owlet.SPARQLComposer._
import com.hp.hpl.jena.query.QueryExecutionFactory
import org.semanticweb.owlapi.model.OWLOntology
import scala.collection.JavaConversions._
import org.semanticweb.owlapi.model.OWLAxiom

object DataUtil {

  def propagatePhenotypes(model: Model): Model = {
    val query = construct(t('specimen, rdfType, 'phenotype)) where (
      bgp(
        t('otu, denotes, 'specimen),
        t('cell, belongs_to_TU, 'otu),
        t('cell, has_state, 'state),
        t('state, rdfType, 'restriction),
        t('restriction, owlOnProperty, denotes),
        t('restriction, owlAllValuesFrom, 'phenotype)))
    QueryExecutionFactory.create(query, model).execConstruct
  }

  def attributeTerms(model: Model): Set[OWLClass] = {
    val query = select('term) where (bgp(t('term, in_subset, attribute_slim)))
    QueryExecutionFactory.create(query, model).execSelect.map(result => Class(result.getResource("term").getURI)).toSet
  }

  def generateSimilaritySubsumers(store: SemStore): Set[OWLAxiom] = (for {
    entity <- store.reasoner.getSubClasses(AnatomicalEntity, false).getFlattened
    attribute <- attributeTerms(store.model)
    axiom <- Set(entityWithQuality(entity, attribute), entityAndPartsWithQuality(entity, attribute))
  } yield axiom).toSet

  def entityWithQuality(entity: OWLClass, quality: OWLClass) = name(has_part some (entity and (bearer_of some quality)))

  def entityAndPartsWithQuality(entity: OWLClass, quality: OWLClass) = name(has_part some ((part_of some entity) and (bearer_of some quality)))

  private def name(expression: OWLClassExpression) = OntologyUtil.nextClass() EquivalentTo expression

} 