package com.training.shoplocal.repository

import com.training.shoplocal.classes.Product

interface DAOinterface {
    var accessUser: AccessUserInterface
    var databaseCRUD: DatabaseCRUDInterface
}