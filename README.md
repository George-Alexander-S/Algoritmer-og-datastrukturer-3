# Obligatorisk oppgave 3 i Algoritmer og Datastrukturer

Denne oppgaven er en innlevering i Algoritmer og Datastrukturer. 
Oppgaven er levert av følgende student:

* George Alexander Saavedra - s360536


# Oppgavebeskrivelse

Mer detaljerte beskrivelser er å finne i kildekodekommentarene

* Oppgave 1:

Oppgave 1 tar utgangspunkt i kompendiets programkode 5.2.3 a). Til forskjell fra koden fra kompendiet så tar denne høyde
for foreldrepekeren (q) når den nye noden instansieres. Instansieringen av den nye noden tar også høyde for å ta med venstre og høyre barn
slik konstruktøren i obligen krever. Disse er derimot foreløpig "null"

* Oppgave 2:

Oppgave 2 bruker en løsning veldig lik oppgavene til avsnitt 5.2.6, oppgave 2.

En if setning sjekker med inneholder() metoden at verdien i det hele tatt er i treet. Dersom den ikke finnes så
returneres forekomster (som da er instansiert som 0) med en gang.

En while setning fortsetter søket etter verdien med compare(). Forekomster inkrementeres ved treff.

Til slutt returneres forekomster verdien, som da er antallet ganger innparameteren verdi er å finne i treet.

* Oppgave 3:

Første metoden ble greit løst med programkode 5.1.7 h)

Noen endringer måtte til da programkoden ikke tar en innparameter, men det gjør obligens metode. Videre returnerer denne
ikke nodens verdi men selve noden.

Den andre metoden ble løst av en rekke med if setninger som ble skrevet utifra følgende regler fra kompendiet 
om den neste i postorden:

Postorden:

* Hvis p ikke har en forelder ( p er rotnoden), så er p den siste i postorden.
* Hvis p er høyre barn til sin forelder f, er forelderen f den neste.
* Hvis p er venstre barn til sin forelder f, gjelder:
  * Hvis p er enebarn (f.høyre er null), er forelderen f den neste.
  * Hvis p ikke er enebarn (dvs. f.høyre er ikke null), så er den neste den noden som kommer først i postorden i subtreet med f.høyre som rot.


* Oppgave 4

Første metoden ble løst ved å følge instruksene i oppgaven en etter en, og kalle på metodene førstePostorden(), utforOppgave() og
nestePostorden(). En while løkke finner de resterende nodene etter at man etablerer rotnoden.

Den andre metoden ble løst likt oppgave 7 av kompendiets "oppgaver til avsnitt 5.1.7".
Den følger huskeregelen for postorden "venstre, høyre, node" og traverserer dermed treet i postorden rekkefølge.

* Oppgave 5

serialize() metoden:

Denne metoden tar i bruk ArrayList(listen) og ArrayDeque(køen)

Listen tar imot nodene i treet, mens køen holder på nodene til de legges til i listen. Treet traverseres med en while
setning såfremt køen ikke er tom. Før listen deretter returneres.

deserialize() metoden:

Opprinnelig løste jeg denne med ca 30 linjer lang kode uten bruk av leggInn metoden. Det passerte testene og koden er beholdt men
kommentert ut. Løsningen jeg imidlertid beholdt som aktiv er helt enkelt å instansiere et binærtre og bruke en forenklet for-løkke 
som legger inn verdier fra arrayet med leggInn() metoden, før treet returneres.

* Oppgave 6

fjern(T verdi) ble løst som oppgaveteksten foreslo, med utgangspunkt i programkode 5.2.8 d), som 
ble endret til å ta hensyn til foreldrepekere, som da måtte enten fjernes eller endres, alt ettersom.

fjernAlle(T verdi) ble løst med oppgave 3 fra avsnitt 5.2.8. Den bruker gjentatte kall på fjern metoden,
teller opp antallFjernede verdier før de så returneres.

nullstill() metoden sleit jeg unødvendig mye med. Men da jeg fant ut at det var lov å opprette egne metoder
så gikk det veldig greit. Metoden i skallkoden kaller på en rekursiv hjelpemetode jeg laget, som etter gjentatte
kall på seg selv da nullstiller alle pekere og verdier i nodene. 

