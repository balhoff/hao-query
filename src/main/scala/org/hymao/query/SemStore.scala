package org.hymao.query

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import scala.collection.JavaConversions._
import org.apache.commons.io.FileUtils
import org.phenoscape.owl.util.NullIRIMapper
import org.phenoscape.owlet.Owlet
import org.semanticweb.HermiT.Reasoner.ReasonerFactory
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.io.FileDocumentSource
import org.semanticweb.owlapi.model.AddImport
import org.semanticweb.owlapi.model.MissingImportHandlingStrategy
import org.semanticweb.owlapi.model.OWLOntology
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration
import org.semanticweb.owlapi.model.OWLOntologyManager
import org.semanticweb.owlapi.reasoner.OWLReasoner
import com.hp.hpl.jena.query.Query
import com.hp.hpl.jena.query.QueryExecutionFactory
import com.hp.hpl.jena.query.QueryFactory
import com.hp.hpl.jena.query.ResultSet
import com.hp.hpl.jena.rdf.model.Model
import com.hp.hpl.jena.rdf.model.ModelFactory
import org.semanticweb.owlapi.model.AxiomType
import org.semanticweb.elk.owlapi.ElkReasonerFactory

class SemStore(val ontDir: File) {

  private val factory = OWLManager.getOWLDataFactory
  private val loaderConfig = new OWLOntologyLoaderConfiguration().setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT)

  val ontology: OWLOntology = loadOntologyFromFolder(ontDir)

  val tbox: OWLOntology = ontology.getOWLOntologyManager.createOntology(ontology.getTBoxAxioms(true))

  //val hermit: OWLReasoner = new ReasonerFactory().createReasoner(tbox)
  val reasoner: OWLReasoner = new ElkReasonerFactory().createReasoner(ontology)

  val model: Model = loadModelFromFolder(ontDir)

  def select(queryText: String): ResultSet = select(QueryFactory.create(queryText))

  def select(query: Query): ResultSet = {
    val expandedQuery = owlet.expandQuery(query)
    QueryExecutionFactory.create(expandedQuery, model).execSelect
  }

  private val owlet = new Owlet(reasoner)

  private def loadModelFromFolder(folder: File): Model = {
    val jenaModel = ModelFactory.createDefaultModel()
    FileUtils.listFiles(folder, null, true).foreach(loadTriplesIntoModel(jenaModel, _))
    jenaModel
  }

  private def loadTriplesIntoModel(jenaModel: Model, file: File): Unit = {
    val input = new BufferedInputStream(new FileInputStream(file))
    jenaModel.read(input, null)
    input.close()
  }

  private def loadOntologyFromFolder(folder: File): OWLOntology = {
    val manager = createOntologyFolderManager()
    FileUtils.listFiles(folder, null, true).foreach(loadOntologyFromLocalFile(manager, _))
    val onts = manager.getOntologies
    if (onts.size == 1) onts.head
    else importAll(manager)
  }

  private def loadOntologyFromLocalFile(manager: OWLOntologyManager, file: File): Unit = manager.loadOntologyFromOntologyDocument(new FileDocumentSource(file), loaderConfig)

  private def createOntologyFolderManager(): OWLOntologyManager = {
    val manager = OWLManager.createOWLOntologyManager()
    manager.clearIRIMappers()
    manager.addIRIMapper(NullIRIMapper)
    manager
  }

  private def importAll(manager: OWLOntologyManager): OWLOntology = {
    val onts = manager.getOntologies
    val newOnt = manager.createOntology
    for (ont <- onts)
      manager.applyChange(new AddImport(newOnt, factory.getOWLImportsDeclaration(ont.getOntologyID.getOntologyIRI)))
    newOnt
  }

}