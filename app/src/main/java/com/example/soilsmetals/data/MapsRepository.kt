package com.example.soilsmetals.data

import android.content.Context
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import java.io.File

interface MapsRepository {
    fun providedContext(): Context
    fun currentUser(): FirebaseUser?
    fun signOut()
    suspend fun requestAllDocumentsAtDirectory(path: String): Task<QuerySnapshot>
    suspend fun requestToCreateDocumentAtDirectory(
        path: String,
        name: String,
        hashMap: HashMap<String, Any>
    ): Task<Void>

    suspend fun requestToUploadImageAtDirectory(path: String, name: String, uri: Uri): UploadTask
    suspend fun requestToReplaceThisFileData(
        name: String,
        file: File
    ): FileDownloadTask

    suspend fun requestToDeleteDocumentAtDirectory(path: String, name: String): Task<Void>
    suspend fun requestSignIn(email: String, password: String): Task<AuthResult>
    suspend fun requestSignUp(email: String, password: String): Task<AuthResult>
}

class NetworkMapsRepository(private val context: Context) : MapsRepository {
    private val firestore = Firebase.firestore
    private val firestorageRef = Firebase.storage.reference
    private val auth = Firebase.auth

    /*init {
        firestore.useEmulator("10.0.2.2", 8080)
        Firebase.storage.useEmulator("10.0.2.2", 9199)
        auth.useEmulator("10.0.2.2", 9099)
        /*firestore.clearPersistence()*/
    }*/

    override fun providedContext(): Context {
        return context
    }

    override fun currentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun signOut() {
        auth.signOut()
    }

    override suspend fun requestAllDocumentsAtDirectory(path: String): Task<QuerySnapshot> {
        return firestore.collection(path).get()
    }

    override suspend fun requestToCreateDocumentAtDirectory(
        path: String,
        name: String,
        hashMap: HashMap<String, Any>
    ): Task<Void> {
        return firestore.collection(path).document(name).set(hashMap)
    }

    override suspend fun requestToUploadImageAtDirectory(
        path: String,
        name: String,
        uri: Uri
    ): UploadTask {
        return firestorageRef.child("$path/$name.png").putFile(uri, storageMetadata {
            contentType = "image/png"
        })
    }

    override suspend fun requestToReplaceThisFileData(
        name: String,
        file: File
    ): FileDownloadTask {
        return firestorageRef.child("Maps/$name.png").getFile(file)
    }

    override suspend fun requestToDeleteDocumentAtDirectory(
        path: String,
        name: String
    ): Task<Void> {
        return firestore.collection(path).document(name).delete()
    }

    override suspend fun requestSignIn(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    override suspend fun requestSignUp(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }
}