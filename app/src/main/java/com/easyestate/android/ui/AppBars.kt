package com.easyestate.android.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.easyestate.android.AppDestination

private data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

private val NavigationItems = listOf(
    NavigationItem("Home", Icons.Outlined.Home, AppDestination.Home.route),
    NavigationItem("Properties", Icons.Outlined.Apartment, AppDestination.Properties.route),
    NavigationItem("Finances", Icons.Outlined.AccountBalanceWallet, AppDestination.Finances.route),
    NavigationItem("Account", Icons.Outlined.AccountCircle, "account") // Assuming this will be a future route
)

@Composable
fun HomeNavigationBar(
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = NavigationItems

    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = selectedItem == index
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    selectedItem = index
                    onNavigate(item.route)
                },
                icon = { Icon(imageVector = item.icon, contentDescription = item.title, modifier = Modifier.size(30.dp)) },
                label = { Text(item.title, style = MaterialTheme.typography.bodySmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    }
}
