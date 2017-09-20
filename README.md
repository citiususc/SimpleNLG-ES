SimpleNLG-ES
=========

SimpleNLG-ES es una API de Java diseñada para facilitar la generación de textos en lenguaje natural en español. Es una adaptación bilingüe inglés / español de la librería SimpleNLG v4.4.8, siguiendo la estructura utilizada en SimpleNLG-EnFr. La librería SimpleNLG original fue desarrollada para el idioma inglés en el [Department of Computing Science de la University of Aberdeen](https://www.abdn.ac.uk/ncs/departments/computing-science/natural-language-generation-187.php). SimpleNLG-EnFr es su adaptación bilingüe Inglés-Francés, desarrollada en la [Université de Montreal](http://www-etud.iro.umontreal.ca/~vaudrypl/snlgbil/snlgEnFr_francais.xhtml).

SimpleNLG-ES está diseñada para facilitar las tareas de realización lingüística en español en los sistemas de generación de lenguaje natural.

Para empezar
------------
Para obtener información sobre cómo usar SimpleNLG-ES, consulta la Wiki y la API.

Licencia SimpleNLG-ES
-----------------------------
Al estar basada en SimpleNLG y SimpleNLG-EnFr, SimpleNLG-ES está licenciada bajo los términos y condiciones de la [Mozilla Public Licence (MPL) Version 1.1](http://www.mozilla.org/MPL/).


Cita
----
La librería SimpleNLG-ES fue presentada en la "[10th International Conference on Natural Language Generation (INLG2017)](https://eventos.citius.usc.es/inlg2017)", celebrada en Santiago de Compostela. Si utilizas SimpleNLG-ES en algún proyecto, agradecemos que cites el trabajo donde se describe:

> Alejandro Ramos-Soto, Julio Janeiro y Alberto Bugarín, "[Adapting SimpleNLG to Spanish](https://eventos.citius.usc.es/inlg2017/resources/final/51/51_Paper.pdf)". Proceedings of the 10th International Conference on Natural Language Generation (INLG2017), 4-7 Septiembre 2017.

```
@inproceedings{aramossoto2017adapting,
	title = {Adapting {SimpleNLG} to Spanish},
	journal = {10th International Conference on Natural Language Generation},
	year = {2017},
	abstract = {We describe SimpleNLG-ES, an adaptation of the SimpleNLG realization library for the Spanish language. Our implementation is based on the bilingual English-French SimpleNLG-EnFr adaptation. The library has been tested using a battery of examples that ensure that the most common syntax, morphology and orthography rules for Spanish are met. The library is currently being used in three different projects for the development of data-to-text systems in the meteorological, statistical data information, and business intelligence application domains.},
	author = {A. Ramos-Soto and J. Janeiro-Gallardo and Alberto Bugar\'{i}n}
}  
```

SimpleNLG
------------------
La versión actual de SimpleNLG es V4.4.8 ([API](https://cdn.rawgit.com/simplenlg/simplenlg/master/docs/javadoc/index.html)). Para mayor información, vistita su [página de Github](https://github.com/simplenlg/simplenlg) o la [lista de discusión de SimpleNLG] (https://groups.google.com/forum/#!forum/simplenlg) para más detalles.

Si deseas citar SimpleNLG en una publicación académica, utiliza el siguiente documento:

* A Gatt and E Reiter (2009). [SimpleNLG: A realisation engine for practical applications](http://aclweb.org/anthology/W/W09/W09-0613.pdf). Proceedings of ENLG-2009

Si tienes otras preguntas sobre SimpleNLG, ponte en contacto con [Ehud Reiter (Universidad de Aberdeen)](https://www.abdn.ac.uk/ncs/profiles/e.reiter/).

SimpleNLG-EnFr
-----------------------------
La versión actual de SimpleNLG-EnFr es V1.1. SimpleNLG-EnFr puede producir textos en inglés y francés en el mismo documento. La parte francesa cubre prácticamente toda la gramática en [Le français fondamental (1er degré)](http://fr.wikipedia.org/wiki/Fran%C3%A7ais_fondamental) y tiene un lexicón de 3871 entradas que cubre la [escala ortográfica Dubois Buyse](http://o.bacquet.free.fr/db2.htm). Para mayor información, visita su [página de GitHub](https://github.com/rali-udem/SimpleNLG-EnFr).