package com.chpl.router.di

import dagger.Component

@Component(
    modules = [
        RouterModule::class
    ]
)
interface RouterComponent: RouterDependencies