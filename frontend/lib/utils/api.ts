export function apiUrl(url: string): string {
    console.log(process.env.BACKEND_PATH + url);
    return process.env.BACKEND_PATH + url;

}

export async function messageErrorApi(response: Response) {
    const message: string = await response.json();
    return new Error(`API error ${response.status}: ${message}`);
}
