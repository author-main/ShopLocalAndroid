package com.training.shoplocal.dagger

import com.training.shoplocal.AppShopLocal
import dagger.Component
import javax.inject.Scope


@Component(modules = [RepositoryModule::class])
interface RepositoryComponent {
    fun injectApplication(app: AppShopLocal)
}