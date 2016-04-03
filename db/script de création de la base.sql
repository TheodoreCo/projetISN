drop table if exists transition
create table if not exists transition(id_source integer not null, id_cible integer not null)
drop table if exists etat
create table if not exists etat (id integer not null, description varchar(2000) not null, point_entree varchar(200) not null)


insert into etat (id, description, point_entree) values (1, 'Endormi', 'Point de départ ?')
insert into etat (id, description, point_entree) values (2, 'Maintenant à la porte gauche bla bla', 'Porte G ?')
insert into etat (id, description, point_entree) values (3, 'Maintenant à la porte droite bla bla', 'Porte D ?')
insert into etat (id, description, point_entree) values (4, 'Maintenant en train de fouiller la bibliothèque', 'Fouiller bibliothèque?')
insert into etat (id, description, point_entree) values (5, 'Vous êtes au Purgatoire. Nanméoh, porte gauche vraiment ??', 'Porte gauche vérouillée. Passer à la porte droite.')


insert into transition(id_source, id_cible) values (1,2)
insert into transition(id_source, id_cible) values (1,3)
insert into transition(id_source, id_cible) values (1,4)
insert into transition(id_source, id_cible) values (2,5)
insert into transition(id_source, id_cible) values (5,3)

/*
Ordre SQL à utiliser pour l'affichage:

select t.id_source, t.id_cible, e.point_entree, e2.description
from transition t, etat e, etat e2
where t.id_cible = e.id
and t.id_source = e2.id
and t.id_source = 1
*/