Example to scrape and keep selected web pages information from Web cv.lv
=======================================================================
Used
----
1. JavaFX webview
2. JSoup for selected html page parsing
3. To keep information the XML parser: SimpleXML is used (SimpleXMLParser.java)
4. To save extracted xml document in PDF format, the ITextPdf is used.
   
There are also available sample classes for Dom Parser (DomParser.java) and
XStream parser (XSteamParser.java -> XStreamTransformer.java). 

XStream drivers
---------------
According to http://x-stream.github.io/tutorial.html

To use XStream, simply instantiate the XStream class:
~~~~
XStream xstream = new XStream();
~~~~
You require xstream-[version].jar, xpp3-[version].jar and xmlpull-[version].jar in the classpath. 
Xpp3 is a very fast XML pull-parser implementation. 

**If you do not want to include these dependencies,**
you can use a standard JAXP DOM parser or since Java 6 the integrated StAX parser instead:
~~~~
XStream xstream = new XStream(new DomDriver()); // does not require XPP3 library

XStream xstream = new XStream(new StaxDriver()); // does not require XPP3 library starting with Java 6
~~~~
Stax like Sax uses less memory.