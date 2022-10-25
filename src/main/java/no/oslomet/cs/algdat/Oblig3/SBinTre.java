package no.oslomet.cs.algdat.Oblig3;


import java.util.ArrayList;
import java.util.Comparator;

import java.util.StringJoiner;

public class SBinTre<T> {
    private static final class Node<T>   // en indre nodeklasse
    {
        private T verdi;                   // nodens verdi
        private Node<T> venstre, høyre;    // venstre og høyre barn
        private Node<T> forelder;          // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder) {
            this.verdi = verdi;
            venstre = v;
            høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder)  // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString() {
            return "" + verdi;
        }

    } // class Node

    private Node<T> rot;                            // peker til rotnoden
    private int antall;                             // antall noder
    private int endringer;                          // antall endringer

    private final Comparator<? super T> comp;       // komparator

    public SBinTre(Comparator<? super T> c)    // konstruktør
    {
        rot = null;
        antall = 0;
        comp = c;
    }

    public boolean inneholder(T verdi) {
        if (verdi == null) return false;

        Node<T> p = rot;

        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }

        return false;
    }

    public int antall() {
        return antall;
    }

    public String toStringPostOrder() {
        if (tom()) return "[]";

        StringJoiner s = new StringJoiner(", ", "[", "]");

        Node<T> p = førstePostorden(rot); // går til den første i postorden
        while (p != null) {
            s.add(p.verdi.toString());
            p = nestePostorden(p);
        }

        return s.toString();
    }

    public boolean tom() {
        return antall == 0;
    }


    //Oppgave 1
    public boolean leggInn(T verdi) { // Utgangspunkt i kompendiets programkode 5.2.3 a


        Node<T> p = rot, q = null;               // p starter i roten
        int cmp = 0;                             // hjelpevariabel

        while (p != null)       // fortsetter til p er ute av treet
        {
            q = p;                                 // q er forelder til p
            cmp = comp.compare(verdi,p.verdi);     // bruker komparatoren
            p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
        }

        // p er nå null, dvs. ute av treet, q er den siste vi passerte

        p = new Node<T>(verdi, null, null, q);                   // oppretter en ny node

        if (q == null) rot = p;                  // p blir rotnode
        else if (cmp < 0) q.venstre = p;         // venstre barn til q
        else q.høyre = p;                        // høyre barn til q

        antall++;                                // én verdi mer i treet
        return true;                             // vellykket innlegging
    }

    public boolean fjern(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public int fjernAlle(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    // Oppgave 2
    public int antall(T verdi) { // Inspirert av kompendiet - oppgaver til avsnitt 5.2.6, oppgave 2
        int forekomster = 0;

        if (!inneholder(verdi)) {
            return forekomster;   // Returnerer null(forekomster) med en gang dersom verdien ikke finnes i treet.
        }
        else {
            Node<T> p = rot;

            while (p != null) {
                int cmp = comp.compare(verdi, p.verdi);
                if (cmp < 0) {      // Dersom verdien er mindre enn nåværende noden, kan ikke verdien ligge i høyre-
                                    // delen av treet. Dermed fortsetter vi søket på venstre siden.
                    p = p.venstre;
                }
                else {
                    if (cmp == 0) {
                        forekomster++;  // Dersom vi skulle få et treff
                    }
                    p = p.høyre;    // Dersom verdien vi søker er større enn nåværende, må den forekomme på høyre del av treet.
                }
            }
        }
        return forekomster; // Returnerer antallet forekomster
    }

    public void nullstill() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    //Oppgave 3
    // Basert på programkode 5.1.7 h)
    private static <T> Node<T> førstePostorden(Node<T> p) {

       while(true) {
           if (p.venstre != null) {
               p = p.venstre;
           }
           else if (p.høyre != null) {
               p = p.høyre;
           }
           else {
               return p;
           }
       }
    }
    //Oppgave 3
   /* Denne metoden er løst utifra prinsippet som står i kompendiet angående "den neste" for postorden.
    Jeg siterer kompendiet nedenfor:

    - "Hvis P ikke har en forelder (p er rotnoden), så er p den siste i postorden.
    - "Hvis p er høyre barn til sin forelder f, er forelderen f den neste.
    - "Hvis p er venstre barn til sin forelder f, gjelder:
            - hvis p er enebarn (f.høyre er null), er forelderen f den neste.
            - hvis p ikke er enebarn (dvs. f.høyre er ikke null), så er den neste den noden som kommer først
              i postorden i subtreet med f.høyre som rot."

    If setningene i metoden nedenfor er laget med disse reglene som utgangspunkt.
    Løkken i metoden er den samme som i førstePostorden() metoden, som da er lånt fra programkode 5.1.7 h)*/
    private static <T> Node<T> nestePostorden(Node<T> p) {
        Node<T> neste = p;
        if (p.forelder == null) {
            neste = null;
        }
        else {
            if (p.forelder.høyre == p) {
                neste = p.forelder;
            }
            else if (p.forelder.venstre == p) {
                if (p.forelder.høyre == null) {
                    neste = p.forelder;
                }
                else {
                    p = p.forelder.høyre;
                    while(true) {
                    if (p.venstre != null) {
                        p = p.venstre;
                    }
                    else if (p.høyre != null) {
                        p = p.høyre;
                    }
                    else {
                        return p;
                    }}
                }
            }
        }
        return neste;
    }

    // Oppgave 4
    public void postorden(Oppgave<? super T> oppgave) {
        Node<T> p = rot;
        p = førstePostorden(p);
        while(p != null) {
            oppgave.utførOppgave(p.verdi);
            p = nestePostorden(p);
        }
    }

    public void postordenRecursive(Oppgave<? super T> oppgave) {
        postordenRecursive(rot, oppgave);
    }

    // Oppgave 4
    // Denne måten å traversere på er forklart i kompendiet og denne samme kodesnutten er å finne i oppgave 7 av oppgaver i avsnitt 5.1.7
    private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {
        // DET ER DENNE DU SKAL KODE
        if (p.venstre != null) {
            postordenRecursive(p.venstre, oppgave);
        }
        if (p.høyre != null) {
            postordenRecursive(p.høyre, oppgave);
        }
        oppgave.utførOppgave(p.verdi);
    }

    public ArrayList<T> serialize() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    static <K> SBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }


} // ObligSBinTre
