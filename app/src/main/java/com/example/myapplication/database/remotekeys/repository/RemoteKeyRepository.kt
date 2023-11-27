package com.example.myapplication.database.remotekeys.repository

import com.example.myapplication.database.remotekeys.model.RemoteKeyType
import com.example.myapplication.database.remotekeys.model.RemoteKeys

interface RemoteKeyRepository {
    suspend fun getAllRemoteKeys(id: Int, type: RemoteKeyType): RemoteKeys?
    suspend fun createRemoteKeys(remoteKeys: List<RemoteKeys>)
    suspend fun deleteRemoteKey(type: RemoteKeyType)
}