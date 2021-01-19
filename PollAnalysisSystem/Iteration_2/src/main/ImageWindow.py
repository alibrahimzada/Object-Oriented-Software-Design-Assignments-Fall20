
from PyQt5 import QtCore, QtGui, QtWidgets


class Ui_image_MainWindow(object):
    def setupUi(self, image_MainWindow):
        image_MainWindow.setObjectName("image_MainWindow")
        image_MainWindow.resize(1450, 850)
        self.centralwidget = QtWidgets.QWidget(image_MainWindow)
        self.centralwidget.setObjectName("centralwidget")
        self.Image_placeholder = QtWidgets.QLabel(self.centralwidget)
        self.Image_placeholder.setGeometry(QtCore.QRect(30, 50, 1400, 550))
        self.Image_placeholder.setObjectName("Image_placeholder")
        image_MainWindow.setCentralWidget(self.centralwidget)
        self.statusbar = QtWidgets.QStatusBar(image_MainWindow)
        self.statusbar.setObjectName("statusbar")
        image_MainWindow.setStatusBar(self.statusbar)

        self.retranslateUi(image_MainWindow)
        QtCore.QMetaObject.connectSlotsByName(image_MainWindow)

    def retranslateUi(self, image_MainWindow):
        _translate = QtCore.QCoreApplication.translate
        image_MainWindow.setWindowTitle(_translate("image_MainWindow", "MainWindow"))
        self.Image_placeholder.setText(_translate("image_MainWindow", "TextLabel"))
