package lius.lucene;

import org.apache.lucene.analysis.Analyzer;
import java.lang.reflect.Constructor;
import org.apache.log4j.Logger;

/**
public class AnalyzerFactory {
   static Logger logger = Logger.getRootLogger();
  /**
  public static Analyzer getAnalyzer(LiusConfig xc) {
               an = (Analyzer) con[i].newInstance(new Object[] {xc.getStopWord()});
          }
        }
       }
     logger.error("ClassNotFoundException" + e.getMessage());