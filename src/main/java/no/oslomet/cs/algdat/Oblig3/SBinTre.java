package no.oslomet.cs.algdat.Oblig3;


import java.util.ArrayDeque;
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

        if (verdi == null) {
            return false;
        }
        Node<T> p = rot, q = null;               // p starter i roten
        int cmp = 0;                             // hjelpevariabel

        if (!tom()) {               // Såfremt treet ikke er tomt fra før av.
            while (p != null)       // fortsetter til p er ute av treet
            {
                q = p;                                 // q er forelder til p
                cmp = comp.compare(verdi,p.verdi);     // bruker komparatoren
                p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
            }
        }
        // p er nå null, dvs. ute av treet, q er den siste vi passerte

        p = new Node<T>(verdi, null, null, q);   // Den nye noden, her er q referansen til forelder, ref endringene oppgaveteksten krevet.

        if (q == null) rot = p;                  // p blir rotnode
        else if (cmp < 0) q.venstre = p;         // venstre barn til q
        else q.høyre = p;                        // høyre barn til q

        antall++;                                // én verdi mer i treet
        return true;                             // vellykket innlegging
    }

    // Oppgave 6 - har brukt kompendiets programkode 5.2.8 d), men endret det slik at det tar hensyn til foreldre pekerne (som må fjernes eller endres)
    public boolean fjern(T verdi) {

        if (tom()) {
            return false;
        }
        if (verdi == null) {
            return false;  // treet har ingen nullverdier
        }

        Node<T> p = rot, q = null;   // q skal være forelder til p

        while (p != null)            // leter etter verdi
        {
            int cmp = comp.compare(verdi,p.verdi);      // sammenligner og deretter, utifra reglene om søkbare binærtrær:
            if (cmp < 0) { q = p; p = p.venstre; }      // Om verdi er mindre enn p.verdi - går til venstre
            else if (cmp > 0) { q = p; p = p.høyre; }   // Om verdi er større enn p.verdi - går til høyre
            else break;    // den søkte verdien ligger i p
        }
        if (p == null) return false;   // finner ikke verdi

        if (p.venstre == null || p.høyre == null)  // Tilfelle 1) og 2)
        {
            Node<T> b = p.venstre != null ? p.venstre : p.høyre;  // b for barn
            if (p == rot) {
                rot = b;
                if (antall > 1) {       // Her måtte det sjekkes at det er mer enn 1 node i treet. Ellers blir det krøll å fjerne foreldrepekeren!
                    b.forelder = null;
                }
            }
            else if (p == q.venstre) {
                q.venstre = b;
                if (b != null) {
                    b.forelder = q;         // forelder til barn av p må da bli q, som var forelder til p
                }
            }
            else {
                q.høyre = b;
                if (b != null) {
                    b.forelder = q;         // forelder til barn av p må da bli q, som var forelder til p
                }
            }
        }
        else  // Tilfelle 3)
        {
            Node<T> s = p, r = p.høyre;   // finner neste i inorden
            while (r.venstre != null)     // traverserer i inorden med denne while løkken
            {
                s = r;    // s vil stoppe på plassen som er forelder til r
                r = r.venstre;  // r vil nå være noden som er første i inorden etter p.
            }

            p.verdi = r.verdi;   // kopierer verdien i r til p

            if (s != p) {
                s.venstre = r.høyre;
                if (r.høyre != null) {
                    r.høyre.forelder = s;   // setter foreldrepekeren til s.
                }
            }
            else {
                s.høyre = r.høyre;
            }
        }
        antall--;   // det er nå én node mindre i treet
        return true;

    }

    // Oppgave 6 - Denne er løst med oppgave 3 varianten fra avsnitt 5.2.8 med gjentatte kall på fjern
    public int fjernAlle(T verdi) {
        int antallFjernet = 0;
        while (fjern(verdi)) {
            antallFjernet++;        // Antallet fjernede verdier inkrementeres
        }
        return antallFjernet;       // Før de så returneres

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


    // Oppgave 6 sin nullstill metode:
    // Fant ut at det var lov å bruke hjelpemetoder. Da blir dette noe enklere.
    // Oppgaven er løst med et rekursivt kall på den private metoden under.
    // Begge metodene er svært like som løsningen til oppgave 5 i kompendiets oppgaver av avsnitt 5.2.8

    private void nullstill(Node<T> p) {
        if (p.venstre != null) {
            nullstill(p.venstre);
            p.venstre = null;
        }
        if (p.høyre != null) {
            nullstill(p.høyre);
            p.høyre = null;
        }
        p.verdi = null;
        p.forelder = null;
    }

    // Oppgave 6:
    // Dette er metoden fra skallkoden som kaller den private metoden over.
    public void nullstill() {
        if (!tom()) {
            nullstill(rot);
        }
        rot = null;
        antall = 0;
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
                        }
                    }
                }
            }
        }
        return neste;
    }

    // Oppgave 4
    // Denne første metoden er løst ved å følge instruksene i oppgaven en etter en, samt bruke de metodene oppgaven ber oss bruke.
    public void postorden(Oppgave<? super T> oppgave) {
        Node<T> p = rot;
        p = førstePostorden(p); // Etablerer rotnoden
        while(p != null) {      // Så fremt vi er på en node
            oppgave.utførOppgave(p.verdi); // Utfører vi oppgaven
            p = nestePostorden(p);          // Fortsetter til neste node, før løkken starter fra topp igjen.
        }
    }

    public void postordenRecursive(Oppgave<? super T> oppgave) {
        postordenRecursive(rot, oppgave);
    }

    // Oppgave 4
    // Denne måten å traversere på er forklart i kompendiet og denne samme kodesnutten er å finne i oppgave 7 av oppgaver i avsnitt 5.1.7
    private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {
        // Huskeregelen for postorden er "venstre, høyre, deretter noden". Og koden under følger samme regel.
        if (p.venstre != null) {
            postordenRecursive(p.venstre, oppgave);
        }
        if (p.høyre != null) {
            postordenRecursive(p.høyre, oppgave);
        }
        oppgave.utførOppgave(p.verdi);
    }

    // Oppgave 5:
    // Denne oppgaven er løst med ArrayList og ArrayDeque
    public ArrayList<T> serialize() {
        ArrayList<T> treList = new ArrayList<T>();      // Instansierer listen treList
        ArrayDeque<Node<T>> kø = new ArrayDeque<>();    // Lager en kø med ArrayDeque
        kø.add(rot);            // Legger rot i køen

        while (!kø.isEmpty()) {     // Traverserer treet i nivåorden
            Node<T> p = kø.remove(); // Fjerner node fra køen
            treList.add(p.verdi);    // Legger den inn i listen treList

            if (p.venstre != null) { // Traverserer videre med if setningene
                kø.add(p.venstre);   // Og legger til eventuelle noder i køen
            }                        // While setningen vil fortsette til køen er tom.
            if (p.høyre != null) {
                kø.add(p.høyre);
            }
        }
        return treList;             // Returnerer så treList
    }

    // Oppgave 5:
    // Opprinnelig hadde jeg fungerende kode, som var unødvendig lang. Jeg tenkte ikke at fordelen til serialize er at ting allerede
    // er i riktig rekkefølge i listen, og skrev en unødvendig lang kode med en rekke if statements som artig nok bestod testene til oppgave 5.
    // Min opprinnelige fungerende løsning er kommentert ut under følgende metode.
    // En langt smidere løsning er imidlertid valgt til å bli stående.
    static <K> SBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {
        SBinTre<K> tre = new SBinTre<>(c);  // Instansierer et binærtre

        for (K liste : data) {          // Enhanced for loop som legger inn verdier fra arrayet med leggInn() metoden
            tre.leggInn(liste);
        }
        return tre;     // returnerer treet.

        // Den lange kodesnutten under var min opprinnelige løsning til metoden. Da uten bruk av leggInn metoden.
        // Den passerte også testen ^_^

        /*tre.rot = new Node<K>(data.get(0), null, null, null); // Etablerer rot med første element fra listen
        data.remove(0);                            // Fjerner første element fra listen da det er "brukt" på rot.

        int cmp = 0;                                    // Lager en hjelpevariabel

        while (!data.isEmpty()) {                       // Løkken kjører såfremt listen ikke er tom
            Node<K> p = tre.rot, q = null;              // Setter p noden til rot, og q foreløpig til null
            while (p != null) {                         // Traverserer så nedover treet mens det lages
                q = p;                                  // q settes lik p, q blir da forelder til
                cmp = c.compare(data.get(0), p.verdi);  // Med comparatoren så sjekkes neste tall i listen,
                p = cmp < 0 ? p.venstre : p.høyre;      // og finner ut om det skal til venstre eller høyre for sin forelder
            }

            p = new Node<K>(data.get(0), null, null, q);    // Deretter lages den nye noden p med data fra listen. Jeg har satt q som forelder
            if (q == null) {                                      // Sjekker at q ikke er lik null. Dersom den er det, vil dette være rot.
                tre.rot = p;
            }
            else if (cmp < 0) {                                   // Med resultatet fra comparatoren så avgjør vi om den nye noden er venstre,
                q.venstre = p;
            }
            else {                                                // eller høyre barn av forelderen q.
                q.høyre = p;
            }
            tre.antall++;                                         // ikrementerer antallet i treet.
            data.remove(0);                                 // Fjerner første elementet fra listen da vi nettopp brukte den for å lage noden
        }
        return tre;*/
    }
} // ObligSBinTre
