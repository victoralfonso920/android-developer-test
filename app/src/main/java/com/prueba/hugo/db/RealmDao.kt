package com.prueba.hugo.db

import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults

/* Created by
 Victor Hernandez
 on 14/01/2020.
 contact : victoralfonso92@yahoo.com
 */



 fun <T : RealmModel> T.create() {
    val realm = Realm.getDefaultInstance()
    try {
        realm.executeTransaction { it.copyToRealmOrUpdate(this) }
    } catch (e: Exception) {
        println(e.message)
        realm.cancelTransaction()
    }
}

 fun  <T : RealmModel> T.getData(): RealmResults<T> {
    val realm = Realm.getDefaultInstance()
    lateinit var data: RealmResults<T>
    try {
        realm.beginTransaction()
        data = realm.where(javaClass).findAll()
    } catch (e: Exception) {
        realm.cancelTransaction()
    } finally {
        realm.commitTransaction()
    }
    return data
}

fun <T : RealmModel> T.getDataFromId(key: String): RealmResults<T> {
    val realm = Realm.getDefaultInstance()
    lateinit var data: RealmResults<T>
    try {
        realm.beginTransaction()
        data = realm.where(javaClass)
            .equalTo("idCar", key)
            .findAll()
    } catch (e: Exception) {
        println(e.message)
        realm.cancelTransaction()
    } finally {
        realm.commitTransaction()
    }
    return data
}

fun <T : RealmModel> T.clearData() {
    val realm = Realm.getDefaultInstance()
    lateinit var data: RealmResults<T>
    try {
        realm.beginTransaction()
        data = realm.where(javaClass).findAll()
        data.deleteAllFromRealm()
    } catch (e: Exception) {
        println(e.message)
        realm.cancelTransaction()
    } finally {
        realm.commitTransaction()
    }
}

fun <T : RealmModel> T.clearDataFromId(key:String) :Boolean {
    val realm = Realm.getDefaultInstance()
    lateinit var data: RealmResults<T>
    try {
        realm.beginTransaction()
        data = realm.where(javaClass)
            .equalTo("idCar", key)
            .findAll()
        data.deleteAllFromRealm()

    } catch (e: Exception) {
        println(e.message)
        realm.cancelTransaction()
        return false
    } finally {
        realm.commitTransaction()
        return true
    }
}
