package com.easyestate.android.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.easyestate.android.ui.theme.EasyEstateTheme

@Composable
fun HomeScreen(
    adminName: String,
    hasUnreadNotifications: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val quickActions = DashboardQuickActions

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 32.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Welcome back",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        )
                        Text(
                            text = adminName,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        BadgedBox(
                            badge = {
                                if (hasUnreadNotifications) {
                                    Badge()
                                }
                            }
                        ) {
                            IconButton(onClick = { }) {
                                Icon(
                                    imageVector = Icons.Outlined.Notifications,
                                    contentDescription = "Notifications",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Occupancy Overview",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            OccupancyStat(label = "Occupied", value = "48")
                            OccupancyStat(label = "Vacant", value = "12")
                            OccupancyStat(label = "Upcoming", value = "5")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Quick Actions",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(quickActions) { action ->
                        QuickActionTile(action = action)
                    }
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(24.dp))

                NavigationBar(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        selected = true,
                        onClick = { },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Home,
                                contentDescription = "Home"
                            )
                        },
                        label = { Text("Home") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                        )
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Apartment,
                                contentDescription = "Portfolio"
                            )
                        },
                        label = { Text("Portfolio") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.AccountCircle,
                                contentDescription = "Tenants"
                            )
                        },
                        label = { Text("Tenants") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = "Settings"
                            )
                        },
                        label = { Text("Settings") }
                    )
                }
            }
        }
    }
}

@Composable
private fun OccupancyStat(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
private fun UnitOverviewRow(
    units: List<UnitOverview>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        units.forEach { unit ->
            UnitOverviewCard(
                unit = unit,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun UnitOverviewCard(
    unit: UnitOverview,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = unit.label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = unit.value,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
            unit.secondaryLabel?.let { secondary ->
                Text(
                    text = secondary,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

private data class UnitOverview(
    val label: String,
    val value: String,
    val secondaryLabel: String? = null,
)

@Composable
private fun QuickActionTile(
    action: QuickAction,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.aspectRatio(1f),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = action.icon,
                    contentDescription = action.title
                )
                Text(
                    text = action.title,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

private data class QuickAction(
    val title: String,
    val icon: ImageVector,
)

private val DashboardQuickActions = listOf(
    QuickAction("New Lease", Icons.Outlined.ReceiptLong),
    QuickAction("Add Unit", Icons.Outlined.Apartment),
    QuickAction("Analytics", Icons.Outlined.BarChart),
    QuickAction("Profile", Icons.Outlined.AccountCircle),
    QuickAction("Payments", Icons.Outlined.ReceiptLong),
    QuickAction("Maintenance", Icons.Outlined.Settings),
    QuickAction("Reports", Icons.Outlined.BarChart),
    QuickAction("Support", Icons.Outlined.Settings)
)

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    EasyEstateTheme {
        HomeScreen(adminName = "Easy Estate Admin")
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreviewWithNotifications() {
    EasyEstateTheme {
        HomeScreen(adminName = "Easy Estate Admin", hasUnreadNotifications = true)
    }
}
