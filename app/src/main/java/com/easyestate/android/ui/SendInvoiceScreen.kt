package com.easyestate.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.easyestate.android.ui.theme.EasyEstateTheme
import java.text.NumberFormat
import java.util.Locale

data class InvoiceItem(
    val id: Int,
    var type: String = "Rent",
    var description: String = "",
    var quantity: Int = 1,
    var amount: Double = 0.0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendInvoiceScreen(
    onBack: () -> Unit,
    onSendInvoice: () -> Unit,
    modifier: Modifier = Modifier
) {
    var property by rememberSaveable { mutableStateOf("") }
    var tenant by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("") }
    var dueDate by rememberSaveable { mutableStateOf("") }

    var isTaxEnabled by rememberSaveable { mutableStateOf(false) }
    val invoiceItems = remember { mutableStateListOf(InvoiceItem(0, amount = 1200.0), InvoiceItem(1, type = "Water", amount = 50.0)) }
    var taxPercent by rememberSaveable { mutableStateOf("0") }

    val subtotal = invoiceItems.sumOf { it.quantity * it.amount }
    val taxAmount = if (isTaxEnabled) subtotal * (taxPercent.toDoubleOrNull() ?: 0.0) / 100 else 0.0
    val total = subtotal + taxAmount

    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    val formFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
        unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        cursorColor = MaterialTheme.colorScheme.primary,
        focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Send Invoice",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.Close, contentDescription = "Close")
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.size(48.dp)) // Balance the title
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp)
            ) {
                Button(
                    onClick = onSendInvoice,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Send Invoice", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                FormField(label = "Select Property") {
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        TextField(
                            value = property,
                            onValueChange = { property = it },
                            readOnly = true,
                            placeholder = { Text("The Grand Apartments") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = formFieldColors
                        )
                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            listOf("The Grand Apartments", "Lakeside Villa", "Downtown Lofts").forEach { selection ->
                                DropdownMenuItem(text = { Text(selection) }, onClick = {
                                    property = selection
                                    expanded = false
                                })
                            }
                        }
                    }
                }
            }

            item {
                FormField(label = "Search for Tenant") {
                    TextField(
                        value = tenant,
                        onValueChange = { tenant = it },
                        placeholder = { Text("John Doe") },
                        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Search") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = formFieldColors,
                        singleLine = true
                    )
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FormField(label = "Date", modifier = Modifier.weight(1f)) {
                        DateField(value = date, onValueChange = { date = it }, placeholder = "Select Date")
                    }
                    FormField(label = "Due Date", modifier = Modifier.weight(1f)) {
                        DateField(
                            value = dueDate,
                            onValueChange = { dueDate = it },
                            placeholder = "Select Due Date",
                            icon = Icons.Outlined.EventAvailable
                        )
                    }
                }
            }

            item {
                ItemsSection(invoiceItems) {
                    invoiceItems.add(InvoiceItem(id = (invoiceItems.lastOrNull()?.id ?: -1) + 1))
                }, onRemoveItem = { item ->
                    invoiceItems.remove(item)
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Tax (Optional)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Switch(checked = isTaxEnabled, onCheckedChange = { isTaxEnabled = it })
                    }
                    TextField(
                        value = if (isTaxEnabled) taxPercent else "",
                        onValueChange = { if (isTaxEnabled) taxPercent = it },
                        placeholder = { Text("e.g. 5") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        colors = formFieldColors,
                        singleLine = true,
                        enabled = isTaxEnabled
                    )
                }
            }

            item {
                TotalsSummary(
                    subtotal = currencyFormat.format(subtotal),
                    tax = currencyFormat.format(taxAmount),
                    total = currencyFormat.format(total)
                )
            }
        }
    }
}

@Composable
private fun FormField(label: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        content()
    }
}

@Composable
private fun DateField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector = Icons.Outlined.CalendarToday
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        trailingIcon = { Icon(icon, contentDescription = null) },
        modifier = Modifier.fillMaxWidth().clickable { /* TODO: Show Date Picker */ },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        singleLine = true
    )
}

@Composable
private fun ItemsSection(
    items: List<InvoiceItem>,
    onAddItem: () -> Unit,
    onRemoveItem: (InvoiceItem) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Items", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
        Column(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items.forEachIndexed { index, item ->
                InvoiceItemRow(
                    item = item,
                    onItemChange = { updatedItem ->
                        items[index].apply {
                            type = updatedItem.type
                            quantity = updatedItem.quantity
                            amount = updatedItem.amount
                        }
                    },
                    onRemove = {
                        onRemoveItem(item)
                    }
                )
            }
            Button(
                onClick = onAddItem,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Outlined.AddCircle, contentDescription = "Add Item", modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Item", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InvoiceItemRow(
    item: InvoiceItem,
    onItemChange: (InvoiceItem) -> Unit,
    onRemove: () -> Unit
) {
    var type by remember { mutableStateOf(item.type) }
    var description by remember { mutableStateOf(item.description) }
    var quantity by remember { mutableStateOf(item.quantity.toString()) }
    var amount by remember { mutableStateOf(item.amount.toString()) }

    LaunchedEffect(type, description, quantity, amount) {
        onItemChange(
            item.copy(
                type = type,
                description = description,
                quantity = quantity.toIntOrNull() ?: 1,
                amount = amount.toDoubleOrNull() ?: 0.0
            )
        )
    }

    val itemFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
        disabledContainerColor = MaterialTheme.colorScheme.background,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.weight(2f)
            ) {
                TextField(
                    value = type,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor(),
                    shape = RoundedCornerShape(8.dp),
                    colors = itemFieldColors
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    listOf("Rent", "Water", "Deposit", "Other").forEach { selection ->
                        DropdownMenuItem(text = { Text(selection) }, onClick = {
                            type = selection
                            expanded = false
                        }, contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding)
                    }
                }
            }
            TextField(
                value = quantity,
                onValueChange = { quantity = it },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = itemFieldColors,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
            )
            TextField(
                value = amount,
                onValueChange = { amount = it },
                modifier = Modifier.weight(2f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = itemFieldColors,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End)
            )
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Outlined.RemoveCircleOutline,
                    contentDescription = "Remove Item"
                )
            }
        }
        if (type == "Other") {
            TextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Description for 'Other'") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = itemFieldColors,
                singleLine = true
            )
        }
    }
}

@Composable
private fun TotalsSummary(
    subtotal: String,
    tax: String,
    total: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Subtotal", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(subtotal, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Tax", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(tax, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Text(total, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SendInvoiceScreenPreview() {
    EasyEstateTheme {
        SendInvoiceScreen(onBack = {}, onSendInvoice = {})
    }
}