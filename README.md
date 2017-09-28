SimpleNLG-ES
=========

SimpleNLG-ES is a simple Java API designed to facilitate the generation of natural language texts in Spanish. It is a bilingual English/Spanish adaptation of the SimpleNLG v4.4.8 library, following the structure used in SimpleNLG-EnFr. The original SimpleNLG library was developed for the English language in the [Department of Computing Science of the University of Aberdeen](https://www.abdn.ac.uk/ncs/departments/computing-science/natural-language-generation-187.php). SimpleNLG-EnFr is its bilingual English-French adaptation, developed at the [Université de Montreal](http://www-etud.iro.umontreal.ca/~vaudrypl/snlgbil/snlgEnFr_francais.xhtml).

SimpleNLG-ES is designed to facilitate the tasks of linguistic realization in Spanish in natural language generation systems.

Getting started
---------------
For information on how to use SimpleNLG-ES, see the Wiki and the API.

SimpleNLG-ES License
-----------------------------
Being based on SimpleNLG and SimpleNLG-EnFr, SimpleNLG-ES is licensed under the terms and conditions of the [Mozilla Public License (MPL) Version 1.1](https://www.mozilla.org/en-US/MPL/1.1/).

The lexicon used in SimpleNLG-ES is generated from the [FreeLing dictionary](http://nlp.lsi.upc.edu/freeling/) which is licensed under the terms and conditions of the [Lesser General Public License For Linguistic Resources](http://infolingu.univ-mlv.fr/DonneesLinguistiques/Lexiques-Grammaires/lgpllr.html).

Citation
--------
The SimpleNLG-ES library was presented at the "[10th International Conference on Natural Language Generation (INLG2017)](https://eventos.citius.usc.es/inlg2017), held in Santiago de Compostela (4-7 September 2017 )". If you use SimpleNLG-ES in any project, please quote the work where it is described:

> Alejandro Ramos-Soto, Julio Janeiro y Alberto Bugarín, "[Adapting SimpleNLG to Spanish](https://eventos.citius.usc.es/inlg2017/resources/final/51/51_Paper.pdf)". Proceedings of the 10th International Conference on Natural Language Generation (INLG2017), 142-146. ISBN: 978-1-945626-52-4.

```
@inproceedings{aramossoto2017adapting,
	title = {Adapting {SimpleNLG} to Spanish},
	journal = {10th International Conference on Natural Language Generation},
	year = {2017},
	pages = {142-146},
	abstract = {We describe SimpleNLG-ES, an adaptation of the SimpleNLG realization library for the Spanish language. Our implementation is based on the bilingual English-French SimpleNLG-EnFr adaptation. The library has been tested using a battery of examples that ensure that the most common syntax, morphology and orthography rules for Spanish are met. The library is currently being used in three different projects for the development of data-to-text systems in the meteorological, statistical data information, and business intelligence application domains.},
	isbn = {978-1-945626-52-4},
	publisher = {Association for Computational Linguistics},
	author = {A. Ramos-Soto and J. Janeiro-Gallardo and Alberto Bugar\'{i}n}
}
```

SimpleNLG
------------------
The current version of SimpleNLG is V4.4.8 ([API](https://cdn.rawgit.com/simplenlg/simplenlg/master/docs/javadoc/index.html)). For more information, visit his [Github page](https://github.com/simplenlg/simplenlg) or the [SimpleNLG discussion list](https://groups.google.com/forum/#!forum/simplenlg).

If you wish to cite SimpleNLG in an academic publication, please cite the following paper:

* A Gatt and E Reiter (2009). [SimpleNLG: A realisation engine for practical applications](http://aclweb.org/anthology/W/W09/W09-0613.pdf). [Proceedings of the 12th European Workshop on Natural Language Generation (ENLG2009)](http://aclweb.org/anthology/siggen.html#2009_0), 90-93.

If you have other questions about SimpleNLG, please contact Professor Ehud Reiter via email: [ehud.reiter@arria.com](mailto:ehud.reiter@arria.com).

SimpleNLG-EnFr
-----------------------------
The current version of SimpleNLG-EnFr is V1.1. SimpleNLG-EnFr can realize text in both English and French in the same document. The French part covers practically all the grammar in [Le français fondamental (1er degré)](http://fr.wikipedia.org/wiki/Fran%C3%A7ais_fondamental) and has a 3871 entry lexicon covering the [échelle orthographique Dubois Buyse](http://o.bacquet.free.fr/db2.htm).