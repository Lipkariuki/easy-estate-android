package com.easyestate.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.GroupAdd
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.automirrored.outlined.NoteAdd
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easyestate.android.AppDestination
import com.easyestate.android.ui.components.HomeNavigationBar
import com.easyestate.android.ui.theme.StitchInfo
import com.easyestate.android.ui.theme.EasyEstateTheme

@Composable
fun HomeScreen(
    adminName: String,
    hasUnreadNotifications: Boolean = false,
    canManageTenants: Boolean = false,
    onLogout: () -> Unit,
    onAddTenantClick: () -> Unit,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            HomeHeader(
                adminName = adminName,
                hasUnreadNotifications = hasUnreadNotifications,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                onLogout = onLogout
            )
        },
        bottomBar = {
            HomeNavigationBar(
                onNavigate = onNavigate
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                OccupancyCard()
            }
            item {
                QuickActionsGrid(
                    canManageTenants = canManageTenants,
                    onAddTenantClick = onAddTenantClick,
                    onNavigate = onNavigate
                )
            }
        }
    }
}

@Composable
private fun HomeHeader(
    adminName: String, hasUnreadNotifications: Boolean, onLogout: () -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Good Afternoon,",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = adminName,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            BadgedBox(badge = { if (hasUnreadNotifications) Badge() }) {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications", tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onLogout) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = "Logout",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun OccupancyCircleStat(
    label: String,
    percentage: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$percentage%",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun OccupancyCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val occupiedCount = 45
            val vacantCount = 15
            val total = occupiedCount + vacantCount
            val occupiedPercentage = (occupiedCount * 100 / total)
            val vacantPercentage = (vacantCount * 100 / total)

            OccupancyCircleStat(
                label = "Occupied",
                percentage = occupiedPercentage,
                color = MaterialTheme.colorScheme.primary
            )
            OccupancyCircleStat(
                label = "Vacant",
                percentage = vacantPercentage,
                color = StitchInfo
            )
        }
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
private fun QuickActionsGrid(
    modifier: Modifier = Modifier,
    canManageTenants: Boolean,
    onAddTenantClick: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val actions = if (canManageTenants) {
        DashboardQuickActions
    } else {
        DashboardQuickActions.filterNot { it.title == "Add Tenant" }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        // Fixed the issue by replacing FlowRow with standard layout (Column of Rows)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            actions.chunked(3).forEach { rowActions ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    rowActions.forEach { action ->
                        QuickActionTile(
                            action = action,
                            onClick = {
                                when (action.title) {
                                    "Add Tenant" -> onAddTenantClick()
                                    "Properties" -> onNavigate(AppDestination.Properties.route)
                                    "Send Bills" -> onNavigate(AppDestination.SendInvoice.route)
                                    else -> {}
                                }
                            },
                            modifier = Modifier.width(96.dp)
                        )
                    }
                    // Add spacers for empty slots in the last row to maintain alignment if row count is < 3
                    repeat(3 - rowActions.size) {
                        Spacer(modifier = Modifier.width(96.dp))
                    }
                }
            }
        }
    }
}


@Composable
private fun QuickActionTile(
    action: QuickAction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.extraLarge
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = action.title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = action.title,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            // Added maxLines to prevent text overflow and potential layout issues
            maxLines = 1
        )
    }
}

private val DashboardQuickActions = listOf(
    QuickAction("Send Bills", Icons.AutoMirrored.Outlined.ReceiptLong),
    QuickAction("Broadcast", Icons.Outlined.Campaign),
    // Changed Visitor Log icon to Outlined.Visibility for better semantic meaning
    QuickAction("Visitor Log", Icons.Outlined.Visibility),
    // Changed Notice icon to Outlined.StickyNote2 for better semantic meaning
    QuickAction("Notice", Icons.Outlined.StickyNote2),
    QuickAction("Payment", Icons.Outlined.Payments),
    QuickAction("Support", Icons.Outlined.SupportAgent),
    QuickAction("Properties", Icons.Outlined.Apartment),
    QuickAction("Maintenance", Icons.Outlined.Construction),
    QuickAction("Add Tenant", Icons.Outlined.GroupAdd)
)

private data class QuickAction(
    val title: String,
    val icon: ImageVector
)

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    EasyEstateTheme {
        HomeScreen(
            adminName = "Easy Estate Admin",
            canManageTenants = true,
            onLogout = {},
            onAddTenantClick = {},
            onNavigate = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreviewWithNotifications() {
    EasyEstateTheme {
        HomeScreen(
            adminName = "Philip",
            hasUnreadNotifications = true,
            canManageTenants = false,
            onLogout = {},
            onAddTenantClick = {},
            onNavigate = {}
        )
    }
}
