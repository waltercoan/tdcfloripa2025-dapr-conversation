from typing import Union
from fastapi import FastAPI, Request
from dapr.clients import DaprClient
from dapr.clients.grpc._request import ConversationInput
import uvicorn
import os

app = FastAPI()
DAPR_CONFIGURATION_STORE = os.getenv('DAPR_CONFIGURATION_STORE')


print("|     ____        _   _                     |")
print("|    |  _ \ _   _| |_| |__   ___  _ __      |")
print("|    | |_) | | | | __| '_ \ / _ \| '_ \     |")
print("|    |  __/| |_| | |_| | | | (_) | | | |    |")
print("|    |_|    \__, |\__|_| |_|\___/|_| |_|    |")
print("|           |___/                           |")

@app.post("/api")
async def api(request: Request):
    msg = await request.body()
    print(f"App P received message: {msg}")
    return {"Hello from App P"}

@app.post("/api/chat")
async def api(request: Request):
    msg = await request.body()
    print(f"Message: {msg}")

    with DaprClient() as client:
        inputs = [
            ConversationInput(content=msg, role='user', scrub_pii=True)
        ]

        conversationName = client.get_configuration(store_name=DAPR_CONFIGURATION_STORE, keys=['CONVERSATION-NAME'], config_metadata={})
        
        metadata = {
            'cacheTTL': '10m',
        }

        response = client.converse_alpha1(
            name=conversationName.items['CONVERSATION-NAME'].value, inputs=inputs, temperature=0.7, metadata=metadata
        )

        for output in response.outputs:
            print(f'Result: {output.result}')
        
        return {"result": response.outputs[0].result}



if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=int(8082))