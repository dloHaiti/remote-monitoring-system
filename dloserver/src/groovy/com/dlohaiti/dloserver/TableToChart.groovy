package com.dlohaiti.dloserver

class TableToChart {

  List<List<Object>> convertWithoutLastRow(List<List<Object>> lists) {
    def copy = new ArrayList<List<Object>>(lists)
    copy.remove(lists.size()-1)
    return copy.transpose()
  }
}
