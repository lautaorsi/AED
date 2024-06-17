package aed;

public class ListaEnlazada<T> {
    // Completar atributos privados
    private int longitud;
    private Nodo primero;
    private Nodo ultimo;

    private class Nodo {
        // Completar
        private T valor;
        private Nodo sig;
        private Nodo ant;

        Nodo(T v) {
            valor = v;
        }
    }

    public ListaEnlazada() {
        this.primero = null;
        this.ultimo = null;
        this.longitud = 0;
        // throw new UnsupportedOperationException("No implementada aun");
    }

    public int longitud() {
        return this.longitud;
        // throw new UnsupportedOperationException("No implementada aun");
    }

    public void agregarAdelante(T elem) {
        Nodo nuevo = new Nodo(elem);
        if (this.longitud == 0) {
            nuevo.ant = null;
            nuevo.sig = null;
            this.primero = nuevo;
            this.ultimo = this.primero;

        } else {
            nuevo.ant = null;
            this.primero.ant = nuevo;
            nuevo.sig = this.primero;
            this.primero = nuevo;
        }
        this.longitud += 1;

        // throw new UnsupportedOperationException("No implementada aun");
    }

    public void agregarAtras(T elem) {
        Nodo nuevo = new Nodo(elem);
        if (this.longitud == 0) {
            nuevo.ant = null;
            nuevo.sig = null;
            this.primero = nuevo;
            this.ultimo = this.primero;

        } else {
            nuevo.sig = null;
            this.ultimo.sig = nuevo;
            nuevo.ant = this.ultimo;
            this.ultimo = nuevo;
        }
        this.longitud += 1;
        // throw new UnsupportedOperationException("No implementada aun");
    }

    public T obtener(int i) {
        Nodo actual = this.primero;
        int j = 0;
        while (j < i) {
            actual = actual.sig;
            j += 1;
        }
        return actual.valor;
        // throw new UnsupportedOperationException("No implementada aun");
    }

    public void eliminar(int i) {
        Nodo actual = this.primero;
        int j = 0;
        while (j < i) {
            actual = actual.sig;
            j += 1;
        }
        if (this.longitud > 1) {
            if (actual.ant == null) {
                this.primero = actual.sig;
                this.primero.ant = null;

            } else if (actual.sig == null) {
                this.ultimo = actual.ant;
                this.ultimo.sig = null;

            } else {
                actual.ant.sig = actual.sig;
                actual.sig.ant = actual.ant;
            }
        } else {
            this.primero = null;
            this.ultimo = null;
        }

        this.longitud -= 1;
        // throw new UnsupportedOperationException("No implementada aun");
    }

    public void modificarPosicion(int indice, T elem) {
        Nodo actual = this.primero;
        int j = 0;
        while (j < indice) {
            actual = actual.sig;
            j += 1;
        }
        actual.valor = elem;
        // throw new UnsupportedOperationException("No implementada aun");
    }

    public ListaEnlazada<T> copiar() {
        ListaEnlazada<T> nueva = new ListaEnlazada<T>();
        int j = 0;
        Nodo actual = this.primero;
        while (j < this.longitud) {
            nueva.agregarAtras(actual.valor);
            actual = actual.sig;
            j += 1;
        }
        return nueva;
        // throw new UnsupportedOperationException("No implementada aun");
    }

    public ListaEnlazada(ListaEnlazada<T> lista) {
        ListaEnlazada<T> nueva = new ListaEnlazada<T>();
        int j = 0;
        Nodo actual = lista.primero;
        while (j < lista.longitud) {
            nueva.agregarAtras(actual.valor);
            actual = actual.sig;
            j += 1;
        }
        this.primero = nueva.primero;
        this.ultimo = nueva.ultimo;
        this.longitud = nueva.longitud;
        // throw new UnsupportedOperationException("No implementada aun");
    }

    @Override
    public String toString() {
        Nodo actual = this.primero;
        int j = 0;
        String res = "[";
        while (j < this.longitud - 1) {
            res += actual.valor.toString() + ", ";
            actual = actual.sig;
            j += 1;
        }
        res += actual.valor.toString() + "]";
        return res;
        // throw new UnsupportedOperationException("No implementada aun");
    }

    private class ListaIterador implements Iterador<T> {
        // Completar atributos privados
        int puntero;
        Nodo primero;
        int longitud;
        Nodo ultimo;

        public boolean haySiguiente() {
            boolean res;
            Nodo actual = this.primero;
            int j = 0;
            while (j < this.puntero) {
                actual = actual.sig;
                j += 1;
            }

            if (actual != null) {
                res = true;
            } else {
                res = false;
            }

            return res;
            // throw new UnsupportedOperationException("No implementada aun");
        }

        public boolean hayAnterior() {
            boolean res;
            Nodo actual = this.ultimo;
            int j = 0;
            while (j < this.longitud - this.puntero) {
                actual = actual.ant;
                j += 1;
            }
            if (actual != null) {
                res = true;
            } else {
                res = false;
            }
            return res;
            // throw new UnsupportedOperationException("No implementada aun");
        }

        public T siguiente() {
            T res;
            Nodo actual = this.primero;
            int j = 0;
            while (j < this.puntero) {
                actual = actual.sig;
                j += 1;
            }
            this.puntero += 1;
            res = actual.valor;
            return res;
            // throw new UnsupportedOperationException("No implementada aun");
        }

        public T anterior() {
            T res;
            Nodo actual = this.ultimo;
            int j = 0;
            while (j < this.longitud - this.puntero) {
                actual = actual.ant;
                j += 1;
            }
            this.puntero -= 1;
            res = actual.valor;
            return res;
            // throw new UnsupportedOperationException("No implementada aun");
        }

    }

    public Iterador<T> iterador() {
        ListaIterador nuevoIterador = new ListaIterador();
        nuevoIterador.puntero = 0;
        nuevoIterador.longitud = this.longitud;
        nuevoIterador.primero = this.primero;
        nuevoIterador.ultimo = this.ultimo;

        return nuevoIterador;
        // throw new UnsupportedOperationException("No implementada aun");
    }
}
