(ns slipstream.ui.data.reports
  (:require [net.cgrand.enlive-html :as html]))
  
(def xml-reports (first (html/html-snippet "<ul>
	<li>
	    <a href='parentDir'>..</a>
	</li>
	<li>
	    <a href='report1.tgz'>report1.tgz</a>
	</li>
	<li>
	    <a href='report2.tgz'>report2.tgz</a>
	</li>
</ul>")))
