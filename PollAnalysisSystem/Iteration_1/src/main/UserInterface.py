

import sys
from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtWidgets import QApplication, QMainWindow

class UserInterface(object):
  pass




def main():
    app = QApplication([])
    window = QMainWindow()
    ui = UserInterface()
    ui.setupUi(window)
    window.show()
    sys.exit(app.exec_())

