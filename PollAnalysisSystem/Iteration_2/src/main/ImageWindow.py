
from PyQt5 import QtCore, QtGui, QtWidgets


class Ui_image_MainWindow(object):
      def setupUi(self, image_MainWindow):
        image_MainWindow.setObjectName("image_MainWindow")
        image_MainWindow.resize(1800, 650)

        self.centralwidget = QtWidgets.QWidget(image_MainWindow)
        self.centralwidget.setObjectName("centralwidget")

        self.scrollArea = QtWidgets.QScrollArea(self.centralwidget)
        self.scrollArea.setGeometry(QtCore.QRect(20, 20, 1700, 500))
        self.scrollArea.setWidgetResizable(True)
        self.scrollArea.setObjectName("scrollArea")

        self.scrollAreaWidgetContents = QtWidgets.QWidget()
        self.scrollAreaWidgetContents.setGeometry(QtCore.QRect(0, 0, 509, 169))
        self.scrollAreaWidgetContents.setObjectName("scrollAreaWidgetContents")

        self.horizontalLayout = QtWidgets.QHBoxLayout(self.scrollAreaWidgetContents)
        self.horizontalLayout.setObjectName("horizontalLayout")
        
        self.Image_placeholder = QtWidgets.QLabel(self.scrollAreaWidgetContents)
        self.Image_placeholder.setObjectName("Image_placeholder")

        self.horizontalLayout.addWidget(self.Image_placeholder)
        self.scrollArea.setWidget(self.scrollAreaWidgetContents)
        image_MainWindow.setCentralWidget(self.centralwidget)

        self.retranslateUi(image_MainWindow)
        QtCore.QMetaObject.connectSlotsByName(image_MainWindow)

      def retranslateUi(self, image_MainWindow):
        _translate = QtCore.QCoreApplication.translate
        image_MainWindow.setWindowTitle(_translate("image_MainWindow", "MainWindow"))
        self.Image_placeholder.setText(_translate("image_MainWindow", "TextLabel"))