package com.dlohaiti.dloserver

class TableToChart {

  List<List<Object>> convertWithoutRowsTitled(List<List<Object>> lists, List<String> rowTitles) {
    def defensiveCopy = new ArrayList<List<Object>>(lists)
    for(Iterator<List<Object>> i = defensiveCopy.iterator(); i.hasNext(); ) {
      def row = i.next()
      if(rowTitles.contains(row.first())) {
        i.remove()
      }
    }
    return defensiveCopy.transpose()
  }
}
