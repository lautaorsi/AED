package aed;

public class Carrera { //En trie materias están todas las materias de la carrera
    //Cada materia del trie materias de una carrera debe estar en la lista de sinónimos de dicha materia,
    //en conjunto con la carrera
    private DictTrie<Materia> materias;

    public Carrera() {
        this.materias = new DictTrie<Materia>(); // O(1)
    }

    public void agregar(String nombreMateria, Materia materia) {
        materias.agregar(nombreMateria, materia); // O(|m|)
    }

    public Materia devolver(String nombreMateria) {
        return materias.devolver(nombreMateria); // O(|m|)
    }

    public void remover(String nombreMateria) {
        this.materias.quitar(nombreMateria); // O(|m|)
    }

    public DictTrie<Materia> devolverMaterias() {
        return this.materias;
    }

}