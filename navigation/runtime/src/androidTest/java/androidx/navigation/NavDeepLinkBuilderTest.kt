/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.navigation

import androidx.navigation.test.R
import android.content.Context
import android.os.Bundle
import androidx.navigation.testing.TestNavigator
import androidx.navigation.testing.test
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertWithMessage
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class NavDeepLinkBuilderTest {

    private val targetContext get() = ApplicationProvider.getApplicationContext() as Context

    @Test
    fun fromContextSetGraphXml() {
        val deepLinkBuilder = NavDeepLinkBuilder(targetContext)

        deepLinkBuilder.setGraph(R.navigation.nav_simple)
        deepLinkBuilder.setDestination(R.id.second_test)
        val taskStackBuilder = deepLinkBuilder.createTaskStackBuilder()
        assertEquals("Expected one Intent", 1, taskStackBuilder.intentCount)
    }

    @Test
    fun fromContextSetGraphNavInflater() {
        val deepLinkBuilder = NavDeepLinkBuilder(targetContext)

        val navigatorProvider = NavigatorProvider().apply {
            addNavigator(NavGraphNavigator(this))
            addNavigator(TestNavigator())
        }
        val navInflater = NavInflater(targetContext, navigatorProvider)
        val navGraph = navInflater.inflate(R.navigation.nav_simple)
        deepLinkBuilder.setGraph(navGraph)
        deepLinkBuilder.setDestination(R.id.second_test)
        val taskStackBuilder = deepLinkBuilder.createTaskStackBuilder()
        assertEquals("Expected one Intent", 1, taskStackBuilder.intentCount)
    }

    @Test
    fun fromContextSetGraphProgrammatic() {
        val deepLinkBuilder = NavDeepLinkBuilder(targetContext)

        val navigatorProvider = NavigatorProvider().apply {
            addNavigator(NavGraphNavigator(this))
            addNavigator(TestNavigator())
        }
        val navGraph = navigatorProvider.navigation(startDestination = 1) {
            test(1)
        }
        deepLinkBuilder.setGraph(navGraph)
        deepLinkBuilder.setDestination(1)
        val taskStackBuilder = deepLinkBuilder.createTaskStackBuilder()
        assertEquals("Expected one Intent", 1, taskStackBuilder.intentCount)
    }

    @Test
    fun fromNavController() {
        val navController = NavController(targetContext).apply {
            navigatorProvider.addNavigator(TestNavigator())
            setGraph(R.navigation.nav_simple)
        }
        val deepLinkBuilder = NavDeepLinkBuilder(navController)

        deepLinkBuilder.setDestination(R.id.second_test)
        val taskStackBuilder = deepLinkBuilder.createTaskStackBuilder()
        assertEquals("Expected one Intent", 1, taskStackBuilder.intentCount)
    }

    @Test
    fun pendingIntentEqualsWithSameArgs() {
        val deepLinkBuilder = NavDeepLinkBuilder(targetContext)

        deepLinkBuilder.setGraph(R.navigation.nav_simple)
        deepLinkBuilder.setDestination(R.id.second_test)
        val args = Bundle().apply {
            putString("test", "test")
        }
        deepLinkBuilder.setArguments(args)
        val firstPendingIntent = deepLinkBuilder.createPendingIntent()

        // Don't change anything and generate a new PendingIntent
        val secondPendingIntent = deepLinkBuilder.createPendingIntent()
        assertWithMessage("PendingIntents with the same destination and args should be the same")
            .that(firstPendingIntent)
            .isEqualTo(secondPendingIntent)
    }

    @Test
    fun pendingIntentNotEqualsWithDifferentDestination() {
        val deepLinkBuilder = NavDeepLinkBuilder(targetContext)

        deepLinkBuilder.setGraph(R.navigation.nav_simple)
        deepLinkBuilder.setDestination(R.id.second_test)
        val args = Bundle().apply {
            putString("test", "test")
        }
        deepLinkBuilder.setArguments(args)
        val firstPendingIntent = deepLinkBuilder.createPendingIntent()

        // Change the destination but not the args
        deepLinkBuilder.setDestination(R.id.start_test)
        val secondPendingIntent = deepLinkBuilder.createPendingIntent()
        assertWithMessage("PendingIntents with different destinations should be different")
            .that(firstPendingIntent)
            .isNotEqualTo(secondPendingIntent)
    }

    @Test
    fun pendingIntentNotEqualsWithDifferentArgs() {
        val deepLinkBuilder = NavDeepLinkBuilder(targetContext)

        deepLinkBuilder.setGraph(R.navigation.nav_simple)
        deepLinkBuilder.setDestination(R.id.second_test)
        val args = Bundle().apply {
            putString("test", "test")
        }
        deepLinkBuilder.setArguments(args)
        val firstPendingIntent = deepLinkBuilder.createPendingIntent()

        // Change the args but not the destination
        args.putString("test", "test2")
        val secondPendingIntent = deepLinkBuilder.createPendingIntent()
        assertWithMessage("PendingIntents with different arguments should be different")
            .that(firstPendingIntent)
            .isNotEqualTo(secondPendingIntent)
    }
}
