package ssol.tools.mima.core.ui.widget

import scala.swing._
import scala.swing.event.ButtonClicked

abstract class ListItemsPanel extends BoxPanel(Orientation.Vertical) {

  type Item <: Component

  private abstract class Row(val elem: Item) extends FlowPanel(FlowPanel.Alignment.Left)() {
    vGap = 0
    contents += elem
  }
  
  private class TopRow(elem: Item) extends Row(elem) {
    private val add = new Button {
      icon = images.Icons.add
    }
    listenTo(add)
    reactions += {
      case ButtonClicked(`add`) =>
        addConstraint()
    }
    
    contents += add
  }
  
  private class AnyRow(elem: Item) extends Row(elem) {
    private val remove = new Button {
      icon = images.Icons.remove
    }
    listenTo(remove)
    reactions += {
      case ButtonClicked(`remove`) =>
        removeConstraint(this)
    }
    
    contents += remove
  }
  
  private val view = new BoxPanel(Orientation.Vertical) {
    def +=(r: Row) {
      contents += r
      updateView()
    }

    def -=(r: Row) {
      contents -= r
      updateView()
    }
  }

  contents += view

  final protected def addConstraint() {
    view += (if (view.contents.isEmpty) new TopRow(create()) else new AnyRow(create()))
  }

  private def removeConstraint(r: Row) {
    remove(r.elem)
    view -= r
  }

  private def updateView() {
    repaint()
    revalidate()
  }

  protected def create(): Item

  protected def remove(c: Item): Unit
}
