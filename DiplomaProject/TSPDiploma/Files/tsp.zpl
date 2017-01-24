#
# Travelling Salesmen Problem with weights given in tsp2.dat and vertices in tsp.dat
#
set V   := { read "tsp.dat" as "<1n>" comment "#" };
set E   := { <i,j> in V * V with i < j };
set P[] := powerset(V \ { ord(V,1,1) });
set K   := indexset(P) \ { 0 };

param dist [E] := read "tsp2.dat" as "1n" comment "#";
do print E;
#file contains distances between vertices in separate lines:
# d(0,1) ; d(0,2) ...; d(1,2) ; d(1,3); ... ; d(n-1,n) 

var x[E] binary;

minimize cost: sum <i,j> in E : dist[i,j] * x[i, j];

subto two_connected:
   forall <v> in V do
      (sum <v,j> in E : x[v,j]) + (sum <i,v> in E : x[i,v]) == 2;

subto no_subtour:
   forall <k> in K with card(P[k]) > 2 and card(P[k]) < card(V) - 2 do
      sum <i,j> in E with <i> in P[k] and <j> in P[k] : x[i,j] 
      <= card(P[k]) - 1;