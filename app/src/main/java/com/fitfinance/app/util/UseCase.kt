package com.fitfinance.app.util

import kotlinx.coroutines.flow.Flow

abstract class UseCase<Param, Source> {
    abstract fun execute(param: Param): Flow<Source>

    open operator fun invoke(param: Param) = execute(param)
}