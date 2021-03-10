export class ApiPathUtils {

	public static appendParametersToPath(path: string, ...parameters: string[]): string {
		let pathWithParameters: string = path.trim();
		parameters.forEach(parameter => {
			if (ApiPathUtils.isPathEndingInSlash(pathWithParameters)) {
				pathWithParameters += parameter;
			} else {
				pathWithParameters += "/" + parameter;
			}
		});
		return pathWithParameters;
	}

	public static isPathEndingInSlash(path: string): boolean {
		return path.slice(-1) === "/";
	}

	public static removeSlashFromStartAndEnd(str: string): string {
		let strx: string = str.trim();
		if (strx.startsWith("/")) {
			strx = strx.substring(1);
		}
		if (strx.endsWith("/")) {
			strx = strx.substring(0, strx.lastIndexOf("/"));
		}
		return strx;
	}

	public static removeSlashFromStart(str: string): string {
		let strx: string = str.trim();
		if (strx.startsWith("/")) {
			strx = strx.substring(1);
		}
		return strx;
	}
}