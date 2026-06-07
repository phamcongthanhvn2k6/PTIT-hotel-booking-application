import random
types = ['Standard', 'Superior', 'Deluxe', 'Suite', 'VIP Suite', 'Penthouse']
prices = {'Standard': 1500000, 'Superior': 1800000, 'Deluxe': 2000000, 'Suite': 3500000, 'VIP Suite': 5000000, 'Penthouse': 8000000}
inserts = []
for f in range(1, 11):
    for r in range(1, 6):
        num = f * 100 + r
        if num in [101, 102, 103, 104]: continue
        t = random.choice(types)
        p = prices[t]
        s = 'AVAILABLE' if random.random() > 0.3 else 'UNAVAILABLE'
        inserts.append(f"(1, '{num}', '{t}', {p}.00, '{s}')")

print('INSERT INTO rooms (hotel_id, room_number, room_type, price_per_night, status) VALUES ' + ', '.join(inserts) + ';')
