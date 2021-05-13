package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	private SimpleWeightedGraph<Airport,DefaultWeightedEdge>grafo;
	private ExtFlightDelaysDAO dao;
	private Map<Integer,Airport>idMap;
	
	public Model() {
		dao=new ExtFlightDelaysDAO();
		idMap=new HashMap<>();
		dao.loadAllAirports(idMap);
	}
	
	public void creaGrafo(int distanzaMinima){
		grafo=new SimpleWeightedGraph<Airport,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//aggiungo tutti i vertici
		Graphs.addAllVertices(grafo, idMap.values());
		//aggiungo gli archi
		for(Rotta r:dao.getArchi(distanzaMinima,idMap)) {
			DefaultWeightedEdge e= grafo.getEdge(r.getA1(), r.getA2());
			//se non è presente nè la rotta da 1 a 2 ne da 2 a 1 ,aggiungila
			if(e==null) {
				Graphs.addEdge(grafo,r.getA1(),r.getA2(),r.getPeso());
			}
			//altrimenti aggiorna il peso
			else {
				double vecchioPeso=grafo.getEdgeWeight(e);
				double nuovoPeso=(vecchioPeso+r.getPeso())/2;
				grafo.setEdgeWeight(e, nuovoPeso);
			}
		}
		
	}
	//i seguenti due metodi servono solo per fare dei controlli nel momento della stampa
		public int getNVertici() {
			if(grafo != null)
				return grafo.vertexSet().size();
			
			return 0;
		}
		
		public int getNArchi() {
			if(grafo != null)
				return grafo.edgeSet().size();
			
			return 0;
		}
		
		//metodo per stampare gli archi
		public List<Rotta> ottieniArchi(){
			List<Rotta>stampa=new ArrayList<>();
			for(DefaultWeightedEdge e:grafo.edgeSet()) {
				stampa.add(new Rotta(grafo.getEdgeSource(e),grafo.getEdgeTarget(e),grafo.getEdgeWeight(e)));
			}
			return stampa;
		}

	
}
