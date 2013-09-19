/*******************************************************************************
 * Copyright 2012
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl-3.0.txt
 ******************************************************************************/
package de.tudarmstadt.ukp.dkpro.core.matetools;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.util.JCasUtil.select;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.jcas.JCas;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.morph.Morpheme;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.testing.AssertAnnotations;
import de.tudarmstadt.ukp.dkpro.core.testing.TestRunner;

public class MateMorphTaggerTest
{
    @Test
    public void testGerman()
        throws Exception
    {
        Assume.assumeTrue(Runtime.getRuntime().maxMemory() >= 1000000000);

        JCas jcas = runTest("de", "Wir brauchen ein sehr kompliziertes Beispiel , welches "
                + "möglichst viele Konstituenten und Dependenzen beinhaltet .");

        String[] lemmas = new String[] { "wir", "brauchen", "ein", "sehr", "kompliziert",
                "beispiel", "--", "welcher", "möglichst", "vieler", "konstituent", "und",
                "dependenz", "beinhalten", "--" };

        String[] morphTagsExpected = { "case=nom|number=pl|gender=*|person=1",
                "number=pl|person=1|tense=pres|mood=ind", "case=acc|number=sg|gender=neut", "_",
                "case=acc|number=sg|gender=neut|degree=pos", "case=acc|number=sg|gender=neut", "_",
                "case=acc|number=sg|gender=neut", "_", "case=acc|number=pl|gender=*",
                "case=acc|number=pl|gender=*", "_", "case=acc|number=pl|gender=fem",
                "number=sg|person=3|tense=pres|mood=ind", "_" };

        AssertAnnotations.assertLemma(lemmas, select(jcas, Lemma.class));
        AssertAnnotations.assertMorpheme(morphTagsExpected, select(jcas, Morpheme.class));
    }

    @Test
    public void testFrench()
        throws Exception
    {
        Assume.assumeTrue(Runtime.getRuntime().maxMemory() >= 1000000000);

        JCas jcas = runTest("fr", "Nous avons besoin d'une phrase par exemple très "
                + "compliqué, qui contient des constituants que de nombreuses dépendances et que "
                + "possible .");

        String[] lemmas = new String[] { "il", "avoir", "besoin", "d'une", "phrase", "par",
                "exemple", "très", "compliqué,", "qui", "contenir", "de", "constituant", "que",
                "de", "nombreux", "dépendance", "et", "que", "possible", "." };

        String[] morphTagsExpected = { "g=m|n=p|p=1|s=suj", "m=ind|n=p|p=1|t=pst", "g=m|n=s|s=c",
                "_", "g=f|n=s|s=c", "_", "g=m|n=s|s=c", "_", "g=m|n=s|s=qual", "g=m|n=p|p=3|s=rel",
                "m=ind|n=s|p=3|t=pst", "g=m|n=p|s=ind", "g=m|n=p|s=c", "g=m|n=p|p=3|s=rel",
                "g=f|n=p|s=ind", "g=f|n=p|s=qual", "g=f|n=p|s=c", "s=c", "s=s", "g=m|n=s|s=qual",
                "s=s" };

        AssertAnnotations.assertLemma(lemmas, select(jcas, Lemma.class));
        AssertAnnotations.assertMorpheme(morphTagsExpected, select(jcas, Morpheme.class));
    }

    private JCas runTest(String aLanguage, String aText)
        throws Exception
    {
        Assume.assumeTrue(Runtime.getRuntime().maxMemory() >= 2000000000);

        AnalysisEngineDescription lemma = createEngineDescription(MateLemmatizer.class);
        AnalysisEngineDescription morphTag = createEngineDescription(MateMorphTagger.class);

        AnalysisEngineDescription aggregate = createEngineDescription(lemma, morphTag);

        return TestRunner.runTest(aggregate, aLanguage, aText);
    }

    @Rule
    public TestName name = new TestName();

    @Before
    public void printSeparator()
    {
        System.out.println("\n=== " + name.getMethodName() + " =====================");
    }
}
